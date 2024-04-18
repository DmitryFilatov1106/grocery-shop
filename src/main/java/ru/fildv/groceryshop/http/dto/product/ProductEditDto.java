package ru.fildv.groceryshop.http.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Value
public class ProductEditDto {
    Integer id;

    @Size(min = 3, max = 127, message = "{name.Size}")
    String name;

    @PositiveOrZero(message = "{purchasePrice}")
    @NotNull(message = "{purchasePrice.NotNull}")
    BigDecimal purchasePrice;

    @NotNull(message = "{baseUnit.NotNull}")
    Integer baseUnitId;

    @NotNull(message = "{category.NotNull}")
    Integer categoryId;

    MultipartFile image;
}
