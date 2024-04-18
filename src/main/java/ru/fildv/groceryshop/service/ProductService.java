package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.database.entity.ProductUnit;
import ru.fildv.groceryshop.database.repository.ProductRepository;
import ru.fildv.groceryshop.database.repository.ProductUnitRepository;
import ru.fildv.groceryshop.http.dto.comment.CommentEditDto;
import ru.fildv.groceryshop.http.dto.product.*;
import ru.fildv.groceryshop.http.mapper.product.*;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductReadMapper productReadMapper;
    private final ProductSimpleReadMapper productSimpleReadMapper;
    private final ProductShortReadMapper productShortReadMapper;
    private final ProductChoiceMapper productChoiceMapper;
    private final ProductUpdateMapper productUpdateMapper;
    private final ImageService imageService;
    private final ProductUnitRepository productUnitRepository;

    @Value("${app.page.size}")
    private int sizePage;

    public Page<ProductSimpleReadDto> findAll(ProductFilterDto productFilterDto, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        boolean filterNameNull = Objects.isNull(productFilterDto.getName()) || productFilterDto.getName().equals("");
        boolean filterCategoryNull = Objects.isNull(productFilterDto.getCategoryId());

        if (!filterNameNull && filterCategoryNull)
            return productRepository.findAllByNameContainingIgnoreCase(
                            productFilterDto.getName(), PageRequest.of(page, sizePage, sort)
                    )
                    .map(productSimpleReadMapper::map);

        if (filterNameNull && !filterCategoryNull)
            return productRepository.findAllByCategoryId(
                            productFilterDto.getCategoryId(),
                            PageRequest.of(page, sizePage, sort)
                    )
                    .map(productSimpleReadMapper::map);


        if (!filterNameNull && !filterCategoryNull)
            return productRepository.findAllByNameContainingIgnoreCaseAndCategoryId(
                            productFilterDto.getName().toUpperCase(),
                            productFilterDto.getCategoryId(),
                            PageRequest.of(page, sizePage, sort)
                    )
                    .map(productSimpleReadMapper::map);

        return productRepository.findAll(PageRequest.of(page, sizePage, sort))
                .map(productSimpleReadMapper::map);
    }


    public List<ProductChoiceDto> findPart() {
        return productRepository.findAll().stream()
                .map(productChoiceMapper::map)
                .collect(toList());
    }


    public Optional<ProductReadDto> findById(Integer id) {
        return productRepository.findById(id)
                .map(productReadMapper::map);
    }

    public Optional<ProductReadDto> findByIdCustom(Integer id) {
        return productRepository.findByIdCustom(id)
                .map(productReadMapper::map);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'STOREKEEPER'})")
    public Optional<ProductReadDto> update(Integer id, ProductEditDto productDto) {
        return productRepository.findByIdCustom(id)
                .map(entity -> {
                            uploadImage(productDto.getImage());
                            return productUpdateMapper.map(productDto, entity);
                        }
                )
                .map(productRepository::saveAndFlush)
                .map(entity -> {
                    testBaseUnit(entity);
                    return entity;
                })
                .map(productReadMapper::map);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'STOREKEEPER'})")
    public boolean delete(Integer id) {
        return productRepository.findById(id)
                .map(entity -> {
                    productRepository.delete(entity);
                    productRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'STOREKEEPER'})")
    public ProductReadDto create(ProductEditDto productDto) {
        return Optional.of(productDto)
                .map(dto -> {
                    uploadImage(dto.getImage());
                    return productUpdateMapper.map(productDto);
                })
                .map(productRepository::save)
                .map(entity -> {
                    testBaseUnit(entity);
                    return entity;
                })
                .map(productReadMapper::map)
                .orElseThrow();
    }

    public Map<String, String> getComments(Integer id) {
        var p = productRepository.findComments(id);
        if (p.isPresent()) {
            Map<String, String> m = new LinkedHashMap<>();
            int i = 0;
            for (String s : p.get().getComments()) {
                m.put(String.valueOf(i++), s);
            }
            return m;
        }
        return new HashMap<>();
    }

    @Transactional
    public void addComment(CommentEditDto commentEditDto) {
        productRepository.findById(commentEditDto.getProductId())
                .map(entity -> {
                    entity.getComments().add(commentEditDto.getComment());
                    productRepository.saveAndFlush(entity);
                    return entity;
                }).orElseThrow();
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public boolean deleteComment(Integer id, int commentId) {
        return productRepository.findComments(id)
                .map(entity -> {
                    entity.getComments().remove(commentId);
                    return entity;
                })
                .map(productRepository::saveAndFlush).isPresent();
    }

    public List<ProductShortReadDto> findAllFilter() {
        List<ProductShortReadDto> filter = new ArrayList<>();
        filter.add(new ProductShortReadDto(null, ""));
        filter.addAll(
                productRepository.findAll().stream()
                        .map(productShortReadMapper::map)
                        .sorted(Comparator.comparing(ProductShortReadDto::getName))
                        .toList()
        );
        return filter;

    }

    public Optional<byte[]> findImage(Integer id) {
        return productRepository.findById(id)
                .map(Product::getImage)
                .filter(StringUtils::hasText)
                .flatMap(s -> imageService.get(s, true));
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (image.isEmpty()) return;

        imageService.upload(image.getOriginalFilename(), image.getInputStream(), ImageService.ImageType.PRODUCT);
    }

    private void testBaseUnit(Product entity) {
        var oProductUnit = productUnitRepository.findFirstByProductAndUnit(entity, entity.getBaseUnit());
        if (oProductUnit.isEmpty()) {
            var productUnit = ProductUnit.builder()
                    .product(entity)
                    .unit(entity.getBaseUnit())
                    .ratio(BigDecimal.valueOf(1))
                    .build();
            productUnitRepository.saveAndFlush(productUnit);
        }
    }
}