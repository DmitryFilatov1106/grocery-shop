package ru.fildv.groceryshop.http.dto.stock;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class StockSimpleReadDto {
    Integer id;
    String product;
    String quality;
    String unit;
    BigDecimal storeAmount;
}
