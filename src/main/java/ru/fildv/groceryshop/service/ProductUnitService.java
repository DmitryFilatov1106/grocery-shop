package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.repository.ProductUnitRepository;
import ru.fildv.groceryshop.database.repository.UnitRepository;
import ru.fildv.groceryshop.http.dto.product_unit.ProductUnitEditDto;
import ru.fildv.groceryshop.http.dto.product_unit.ProductUnitReadDto;
import ru.fildv.groceryshop.http.dto.unit.UnitReadDto;
import ru.fildv.groceryshop.http.mapper.product_unit.ProductUnitReadMapper;
import ru.fildv.groceryshop.http.mapper.product_unit.ProductUnitUpdateMapper;
import ru.fildv.groceryshop.http.mapper.unit.UnitReadMapper;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductUnitService {
    private final ProductUnitRepository productUnitRepository;
    private final UnitRepository unitRepository;
    private final ProductUnitReadMapper productUnitReadMapper;
    private final UnitReadMapper unitReadMapper;
    private final ProductUnitUpdateMapper productUnitUpdateMapper;

    public List<ProductUnitReadDto> findAllByProductId(Integer productId) {
        return productUnitRepository.findAllByProductIdWithProductCategoryUnit(productId).stream()
                .map(productUnitReadMapper::map)
                .collect(toList());
    }

    public List<UnitReadDto> findFreeUnits(Integer productId) {
        return unitRepository.findFreeUnits(productId).stream()
                .map(unitReadMapper::map)
                .collect(toList());
    }

    @Transactional
    public boolean delete(Integer id) {
        return productUnitRepository.findById(id)
                .map(entity -> {
                    productUnitRepository.delete(entity);
                    productUnitRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public ProductUnitReadDto create(ProductUnitEditDto dto) {
        return Optional.of(dto)
                .map(productUnitUpdateMapper::map)
                .map(productUnitRepository::save)
                .map(productUnitReadMapper::map)
                .orElseThrow();
    }
}
