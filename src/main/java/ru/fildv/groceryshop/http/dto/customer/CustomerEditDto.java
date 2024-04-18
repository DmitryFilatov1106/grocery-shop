package ru.fildv.groceryshop.http.dto.customer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.LocalDate;

@Value
public class CustomerEditDto {
    Integer id;

    @Size(min = 3, max = 127, message = "{name.Size}")
    String name;

    @Size(min = 3, max = 127, message = "{contract.Size}")
    String contract;

    @NotNull(message = "{customerSince.LocalDate}")
    LocalDate customerSince;

    @NotEmpty
    String status;

    @NotNull(message = "{manager.NotNull}")
    Integer managerId;
}
