package ru.fildv.groceryshop.http.mapper.product;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.http.dto.product.ProductChoiceDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class ProductChoiceMapper implements Mapper<Product, ProductChoiceDto> {
    @Override
    public ProductChoiceDto map(Product from) {
        return new ProductChoiceDto(
                from.getId(),
                from.getName(),
                from.getPurchasePrice()
        );
    }
}
