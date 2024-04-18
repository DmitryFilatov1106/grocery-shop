package ru.fildv.groceryshop.http.mapper.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.http.dto.category.CategoryReadDto;
import ru.fildv.groceryshop.http.dto.product.ProductReadDto;
import ru.fildv.groceryshop.http.dto.unit.UnitReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;
import ru.fildv.groceryshop.http.mapper.category.CategoryReadMapper;
import ru.fildv.groceryshop.http.mapper.unit.UnitReadMapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductReadMapper implements Mapper<Product, ProductReadDto> {
    private final CategoryReadMapper categoryReadMapper;
    private final UnitReadMapper unitReadMapper;

    @Override
    public ProductReadDto map(Product from) {
        CategoryReadDto category = Optional.ofNullable(from.getCategory())
                .map(categoryReadMapper::map)
                .orElse(null);

        UnitReadDto unit = Optional.ofNullable(from.getBaseUnit())
                .map(unitReadMapper::map)
                .orElse(null);

        return new ProductReadDto(
                from.getId(),
                from.getName(),
                from.getPurchasePrice(),
                category,
                from.getStoreAmount(),
                unit,
                from.getImage()
        );
    }
}
