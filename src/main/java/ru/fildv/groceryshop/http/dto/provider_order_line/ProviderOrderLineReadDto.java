package ru.fildv.groceryshop.http.dto.provider_order_line;

import lombok.Value;
import ru.fildv.groceryshop.http.dto.product.ProductReadDto;
import ru.fildv.groceryshop.http.dto.product_unit.ProductUnitReadDto;
import ru.fildv.groceryshop.http.dto.quality.QualityReadDto;

@Value
public class ProviderOrderLineReadDto {
    Integer id;
    Integer amount;
    ProductReadDto product;
    QualityReadDto quality;
    ProductUnitReadDto unit;
}
