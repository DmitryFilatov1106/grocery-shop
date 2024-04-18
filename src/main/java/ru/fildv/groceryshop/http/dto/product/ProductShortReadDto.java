package ru.fildv.groceryshop.http.dto.product;

import lombok.Value;

@Value
public class ProductShortReadDto {
    Integer id;
    String name;
}