package ru.fildv.groceryshop.http.dto.product;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ProductSimpleReadDto {
    Integer id;
    String name;
    BigDecimal purchasePrice;
    String category;
    BigDecimal storeAmount;
    String baseUnit;
    String image;
}