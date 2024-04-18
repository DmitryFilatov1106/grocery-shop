package ru.fildv.groceryshop.http.dto.stock;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class StockEditDto {
    Integer id;
    Integer productId;
    Integer qualityId;
    BigDecimal storeAmount;
}
