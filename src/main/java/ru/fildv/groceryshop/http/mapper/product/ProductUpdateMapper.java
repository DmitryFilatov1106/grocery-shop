package ru.fildv.groceryshop.http.mapper.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.fildv.groceryshop.database.entity.Category;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.database.entity.Unit;
import ru.fildv.groceryshop.database.repository.CategoryRepository;
import ru.fildv.groceryshop.database.repository.UnitRepository;
import ru.fildv.groceryshop.http.dto.product.ProductEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ProductUpdateMapper implements Mapper<ProductEditDto, Product> {
    private final CategoryRepository categoryRepository;
    private final UnitRepository unitRepository;

    @Override
    public Product map(ProductEditDto from, Product to) {
        copy(from, to);
        return to;
    }

    @Override
    public Product map(ProductEditDto from) {
        Product product = new Product();
        copy(from, product);
        return product;
    }

    private void copy(ProductEditDto from, Product to) {
        to.setName(from.getName());
        to.setPurchasePrice(from.getPurchasePrice());
        to.setBaseUnit(getBaseUnit(from.getBaseUnitId()));
        to.setCategory(getCategory(from.getCategoryId()));

        Optional.ofNullable(from.getImage())
                .filter(Predicate.not(MultipartFile::isEmpty))
                .ifPresent(image -> to.setImage(image.getOriginalFilename()));

    }

    private Category getCategory(Integer categoryId) {
        return Optional.ofNullable(categoryId)
                .flatMap(categoryRepository::findById)
                .orElse(null);
    }

    private Unit getBaseUnit(Integer baseUnitId) {
        return Optional.ofNullable(baseUnitId)
                .flatMap(unitRepository::findById)
                .orElse(null);
    }
}
