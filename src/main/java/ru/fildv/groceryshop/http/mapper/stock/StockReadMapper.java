package ru.fildv.groceryshop.http.mapper.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Stock;
import ru.fildv.groceryshop.http.dto.product.ProductReadDto;
import ru.fildv.groceryshop.http.dto.quality.QualityReadDto;
import ru.fildv.groceryshop.http.dto.stock.StockReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;
import ru.fildv.groceryshop.http.mapper.product.ProductReadMapper;
import ru.fildv.groceryshop.http.mapper.quality.QualityReadMapper;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class StockReadMapper implements Mapper<Stock, StockReadDto> {
    private final ProductReadMapper productReadMapper;
    private final QualityReadMapper qualityReadMapper;

    @Override
    public StockReadDto map(Stock from) {
        ProductReadDto customer = Optional.ofNullable(from.getProduct())
                .map(productReadMapper::map)
                .orElse(null);

        QualityReadDto quality = Optional.ofNullable(from.getQuality())
                .map(qualityReadMapper::map)
                .orElse(null);

        return new StockReadDto(
                from.getId(),
                customer,
                quality,
                from.getStoreAmount()
        );
    }
}
