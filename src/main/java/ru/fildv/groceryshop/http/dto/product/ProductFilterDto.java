package ru.fildv.groceryshop.http.dto.product;

import lombok.Value;

@Value
public class ProductFilterDto {
    String name;
    Integer categoryId;
}
