package ru.fildv.groceryshop.http.dto.product_unit;

import lombok.Value;
import ru.fildv.groceryshop.http.dto.product.ProductReadDto;
import ru.fildv.groceryshop.http.dto.unit.UnitReadDto;

import java.math.BigDecimal;

@Value
public class ProductUnitReadDto {
    Integer id;
    BigDecimal ratio;
    ProductReadDto productReadDto;
    UnitReadDto unitReadDto;
}
