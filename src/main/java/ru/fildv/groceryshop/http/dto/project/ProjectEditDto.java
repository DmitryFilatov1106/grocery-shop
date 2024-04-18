package ru.fildv.groceryshop.http.dto.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class ProjectEditDto {
    Integer id;

    @Size(min = 3, max = 64, message = "{name.Size}")
    String name;

    @NotNull(message = "{promanager.NotNull}")
    Integer promanagerId;
}
