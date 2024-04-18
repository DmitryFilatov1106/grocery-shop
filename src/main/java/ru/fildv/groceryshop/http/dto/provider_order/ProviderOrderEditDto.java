package ru.fildv.groceryshop.http.dto.provider_order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.LocalDate;

@Value
public class ProviderOrderEditDto {
    Integer id;

    @NotNull(message = "{document.Date}")
    LocalDate orderDate;

    @Size(max = 255, message = "{document.comment.Size}")
    String comment;

    Boolean commit;
}