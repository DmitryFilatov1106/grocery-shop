package ru.fildv.groceryshop.http.dto.customer;

import lombok.Value;

@Value
public class CustomerFilterDto {
    String name;
    String status;
}
