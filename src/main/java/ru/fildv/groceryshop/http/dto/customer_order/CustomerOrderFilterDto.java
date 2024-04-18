package ru.fildv.groceryshop.http.dto.customer_order;

import lombok.Value;

import java.time.LocalDate;

@Value
public class CustomerOrderFilterDto {
    Integer id;
    LocalDate fromOrderDate;
    LocalDate toOrderDate;
    String comment;
}
