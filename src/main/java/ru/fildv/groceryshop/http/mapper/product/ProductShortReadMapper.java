package ru.fildv.groceryshop.http.mapper.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.http.dto.product.ProductShortReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
@RequiredArgsConstructor
public class ProductShortReadMapper implements Mapper<Product, ProductShortReadDto> {

    @Override
    public ProductShortReadDto map(Product from) {
        return new ProductShortReadDto(
                from.getId(),
                from.getName()
        );
    }
}
