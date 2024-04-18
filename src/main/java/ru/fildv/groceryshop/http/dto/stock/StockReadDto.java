package ru.fildv.groceryshop.http.dto.stock;

import lombok.Value;
import ru.fildv.groceryshop.http.dto.product.ProductReadDto;
import ru.fildv.groceryshop.http.dto.quality.QualityReadDto;

import java.math.BigDecimal;

@Value
public class StockReadDto {
    Integer id;
    ProductReadDto product;
    QualityReadDto quality;
    BigDecimal storeAmount;
}
