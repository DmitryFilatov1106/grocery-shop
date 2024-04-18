package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.database.entity.Quality;
import ru.fildv.groceryshop.database.entity.Stock;
import ru.fildv.groceryshop.database.repository.StockRepository;
import ru.fildv.groceryshop.http.dto.stock.StockFilterDto;
import ru.fildv.groceryshop.http.dto.stock.StockSimpleReadDto;
import ru.fildv.groceryshop.http.mapper.stock.StockSimpleReadMapper;

import java.math.BigDecimal;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StockService {
    private final StockRepository stockRepository;
    private final StockSimpleReadMapper stockSimpleReadMapper;

    @Value("${app.page.size}")
    private int sizePage;

    public Page<StockSimpleReadDto> findAll(StockFilterDto stockFilterDto, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        boolean filterProductNull = Objects.isNull(stockFilterDto.getProductId());
        boolean filterQualityNull = Objects.isNull(stockFilterDto.getQualityId());

        if (!filterProductNull && filterQualityNull)
            return stockRepository.findAllByProductId(
                            stockFilterDto.getProductId(),
                            PageRequest.of(page, sizePage, sort)
                    )
                    .map(stockSimpleReadMapper::map);

        if (filterProductNull && !filterQualityNull)
            return stockRepository.findAllByQualityId(
                            stockFilterDto.getQualityId(),
                            PageRequest.of(page, sizePage, sort)
                    )
                    .map(stockSimpleReadMapper::map);


        if (!filterProductNull && !filterQualityNull)
            return stockRepository.findAllByProductIdAndQualityId(
                            stockFilterDto.getProductId(),
                            stockFilterDto.getQualityId(),
                            PageRequest.of(page, sizePage, sort)
                    )
                    .map(stockSimpleReadMapper::map);

        return stockRepository.findAll(PageRequest.of(page, sizePage, sort))
                .map(stockSimpleReadMapper::map);
    }

    @Transactional
    public void setUpAmount(Product product, Quality quality, BigDecimal amount) {
        var stock = stockRepository.findStockByProductAndQuality(product, quality)
                .map(entity -> {
                    var newAmount = entity.getStoreAmount().add(amount);
                    entity.setStoreAmount(newAmount);
                    return entity;
                })
                .orElse(
                        Stock.builder()
                                .product(product)
                                .quality(quality)
                                .storeAmount(amount)
                                .build()
                );
        stockRepository.saveAndFlush(stock);
    }

    @Transactional
    public void setDownAmount(Product product, Quality quality, BigDecimal amount) {
        var stock = stockRepository.findStockByProductAndQuality(product, quality)
                .map(entity -> {
                    var newAmount = entity.getStoreAmount().subtract(amount);
                    entity.setStoreAmount(newAmount);
                    return entity;
                })
                .orElse(
                        Stock.builder()
                                .product(product)
                                .quality(quality)
                                .storeAmount(BigDecimal.ZERO)
                                .build()

                );
        stockRepository.saveAndFlush(stock);
    }

    public BigDecimal getAmount(Product product, Quality quality) {
        var stockOptional = stockRepository.findStockByProductAndQuality(product, quality);
        if (stockOptional.isPresent()) {
            return stockOptional.get().getStoreAmount();
        }
        return BigDecimal.ZERO;
    }
}
