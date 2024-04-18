package ru.fildv.groceryshop.http.dto.product_unit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class ProductUnitEditDto {
    Integer id;

    @Positive(message = "{ratio.Positive}")
    @NotNull(message = "{ratio.NotNull}")
    BigDecimal ratio;

    Integer productId;
    Integer unitId;
}
