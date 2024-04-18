package ru.fildv.groceryshop.http.dto.customer;

import lombok.Value;

@Value
public class CustomerSimpleReadDto {
    Integer id;
    String name;
    String status;
    String manager;
}
