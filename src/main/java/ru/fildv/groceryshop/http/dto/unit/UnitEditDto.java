package ru.fildv.groceryshop.http.dto.unit;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UnitEditDto {
    Integer id;

    @Size(min = 2, max = 32, message = "{name.Size}")
    String name;

    @Size(min = 1, max = 8, message = "{shortName.Size}")
    String shortName;
}
