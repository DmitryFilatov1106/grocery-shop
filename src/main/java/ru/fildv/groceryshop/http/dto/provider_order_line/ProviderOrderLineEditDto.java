package ru.fildv.groceryshop.http.dto.provider_order_line;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class ProviderOrderLineEditDto {
    Integer id;

    @Positive(message = "{document.amount}")
    @NotNull(message = "{document.amount.NotNull}")
    Integer amount;

    @NotNull(message = "{document.product.NotNull}")
    Integer productId;

    @NotNull(message = "{document.quality.NotNull}")
    Integer qualityId;

    @NotNull(message = "{document.productUnit.NotNull}")
    Integer productUnitId;

    Integer providerOrderId;
}
