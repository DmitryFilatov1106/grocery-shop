package ru.fildv.groceryshop.http.dto.product;

import lombok.Value;
import ru.fildv.groceryshop.http.dto.category.CategoryReadDto;
import ru.fildv.groceryshop.http.dto.unit.UnitReadDto;

import java.math.BigDecimal;

@Value
public class ProductReadDto {
    Integer id;
    String name;
    BigDecimal purchasePrice;
    CategoryReadDto category;
    BigDecimal storeAmount;
    UnitReadDto baseUnit;
    String image;
}