package ru.fildv.groceryshop.http.mapper.product_unit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.database.entity.ProductUnit;
import ru.fildv.groceryshop.database.entity.Unit;
import ru.fildv.groceryshop.database.repository.ProductRepository;
import ru.fildv.groceryshop.database.repository.UnitRepository;
import ru.fildv.groceryshop.http.dto.product_unit.ProductUnitEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductUnitUpdateMapper implements Mapper<ProductUnitEditDto, ProductUnit> {
    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;

    @Override
    public ProductUnit map(ProductUnitEditDto from, ProductUnit to) {
        copy(from, to);
        return to;
    }

    @Override
    public ProductUnit map(ProductUnitEditDto from) {
        ProductUnit productUnit = new ProductUnit();
        copy(from, productUnit);
        return productUnit;
    }

    private void copy(ProductUnitEditDto from, ProductUnit to) {
        to.setRatio(from.getRatio());
        to.setUnit(getUnit(from.getUnitId()));
        to.setProduct(getProduct(from.getProductId()));
    }

    private Product getProduct(Integer productId) {
        return Optional.ofNullable(productId)
                .flatMap(productRepository::findById)
                .orElse(null);
    }

    private Unit getUnit(Integer unitId) {
        return Optional.ofNullable(unitId)
                .flatMap(unitRepository::findById)
                .orElse(null);
    }
}
