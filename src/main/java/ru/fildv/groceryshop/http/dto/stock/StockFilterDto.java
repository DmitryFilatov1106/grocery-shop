package ru.fildv.groceryshop.http.dto.stock;

import lombok.Value;

@Value
public class StockFilterDto {
    Integer productId;
    Integer qualityId;
}
