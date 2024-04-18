package ru.fildv.groceryshop.http.dto.customer_order_line;

import lombok.Value;
import ru.fildv.groceryshop.http.dto.product.ProductReadDto;
import ru.fildv.groceryshop.http.dto.product_unit.ProductUnitReadDto;
import ru.fildv.groceryshop.http.dto.quality.QualityReadDto;

import java.math.BigDecimal;

@Value
public class CustomerOrderLineReadDto {
    Integer id;
    Integer amount;
    ProductReadDto product;
    QualityReadDto quality;
    ProductUnitReadDto unit;
    BigDecimal price;
    BigDecimal sum;
}
