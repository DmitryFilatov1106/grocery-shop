package ru.fildv.groceryshop.http.mapper.product_unit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.ProductUnit;
import ru.fildv.groceryshop.http.dto.product.ProductReadDto;
import ru.fildv.groceryshop.http.dto.product_unit.ProductUnitReadDto;
import ru.fildv.groceryshop.http.dto.unit.UnitReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;
import ru.fildv.groceryshop.http.mapper.product.ProductReadMapper;
import ru.fildv.groceryshop.http.mapper.unit.UnitReadMapper;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProductUnitReadMapper implements Mapper<ProductUnit, ProductUnitReadDto> {
    private final ProductReadMapper productReadMapper;
    private final UnitReadMapper unitReadMapper;

    @Override
    public ProductUnitReadDto map(ProductUnit from) {
        ProductReadDto productReadDto = Optional.ofNullable(from.getProduct())
                .map(productReadMapper::map)
                .orElse(null);

        UnitReadDto unitReadDto = Optional.ofNullable(from.getUnit())
                .map(unitReadMapper::map)
                .orElse(null);

        return new ProductUnitReadDto(
                from.getId(),
                from.getRatio(),
                productReadDto,
                unitReadDto);
    }
}
