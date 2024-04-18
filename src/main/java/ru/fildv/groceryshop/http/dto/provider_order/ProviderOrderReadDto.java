package ru.fildv.groceryshop.http.dto.provider_order;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ProviderOrderReadDto {
    Integer id;

    LocalDate orderDate;
    String sOrderDate;

    Boolean commit;
    String sCommit;

    String comment;
}
