package ru.fildv.groceryshop.http.dto.quality;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class QualityEditDto {
    Integer id;

    @Size(min = 3, max = 32, message = "{name.Size}")
    String name;
}
