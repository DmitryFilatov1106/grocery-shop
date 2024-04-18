package ru.fildv.groceryshop.http.dto.product;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ProductChoiceDto {
    Integer id;
    String name;
    BigDecimal purchasePrice;
}
