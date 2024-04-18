package ru.fildv.groceryshop.http.mapper.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.http.dto.product.ProductSimpleReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
@RequiredArgsConstructor
public class ProductSimpleReadMapper implements Mapper<Product, ProductSimpleReadDto> {

    @Override
    public ProductSimpleReadDto map(Product from) {
        var category = from.getCategory();
        var baseUnit = from.getBaseUnit();

        return new ProductSimpleReadDto(
                from.getId(),
                from.getName(),
                from.getPurchasePrice(),
                category == null ? "-" : category.getName(),
                from.getStoreAmount(),
                baseUnit == null ? "-" : baseUnit.getShortName(),
                from.getImage()
        );
    }
}
