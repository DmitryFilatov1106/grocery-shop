package ru.fildv.groceryshop.http.mapper.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Stock;
import ru.fildv.groceryshop.http.dto.stock.StockSimpleReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@RequiredArgsConstructor
@Component
public class StockSimpleReadMapper implements Mapper<Stock, StockSimpleReadDto> {

    @Override
    public StockSimpleReadDto map(Stock from) {
        return new StockSimpleReadDto(
                from.getId(),
                from.getProduct().getName(),
                from.getQuality().getName(),
                from.getProduct().getBaseUnit().getShortName(),
                from.getStoreAmount()
        );
    }
}
