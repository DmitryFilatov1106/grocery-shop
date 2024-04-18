package ru.fildv.groceryshop.http.dto.provider_order;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ProviderOrderFilterDto {
    Integer id;
    LocalDate fromOrderDate;
    LocalDate toOrderDate;
    String comment;
}
