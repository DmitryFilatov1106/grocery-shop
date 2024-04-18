package ru.fildv.groceryshop.http.mapper.product;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.http.dto.product.ProductEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class ProductEditMapper implements Mapper<Product, ProductEditDto> {
    @Override
    public ProductEditDto map(Product from) {
        return new ProductEditDto(
                from.getId(),
                from.getName(),
                from.getPurchasePrice(),
                from.getBaseUnit().getId(),
                from.getCategory().getId(),
                null
        );
    }
}
