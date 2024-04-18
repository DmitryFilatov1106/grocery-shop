package ru.fildv.groceryshop.http.mapper.customer_order_line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.CustomerOrderLine;
import ru.fildv.groceryshop.http.dto.customer_order_line.CustomerOrderLineReadDto;
import ru.fildv.groceryshop.http.dto.product.ProductReadDto;
import ru.fildv.groceryshop.http.dto.product_unit.ProductUnitReadDto;
import ru.fildv.groceryshop.http.dto.quality.QualityReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;
import ru.fildv.groceryshop.http.mapper.product.ProductReadMapper;
import ru.fildv.groceryshop.http.mapper.product_unit.ProductUnitReadMapper;
import ru.fildv.groceryshop.http.mapper.quality.QualityReadMapper;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomerOrderLineReadMapper implements Mapper<CustomerOrderLine, CustomerOrderLineReadDto> {
    private final ProductUnitReadMapper productUnitReadMapper;
    private final ProductReadMapper productReadMapper;
    private final QualityReadMapper qualityReadMapper;

    @Override
    public CustomerOrderLineReadDto map(CustomerOrderLine from) {
        ProductReadDto productReadDto = Optional.ofNullable(from.getProduct())
                .map(productReadMapper::map)
                .orElse(null);

        QualityReadDto qualityReadDto = Optional.ofNullable(from.getQuality())
                .map(qualityReadMapper::map)
                .orElse(null);

        ProductUnitReadDto productUnitReadDto = Optional.ofNullable(from.getProductUnit())
                .map(productUnitReadMapper::map)
                .orElse(null);

        return new CustomerOrderLineReadDto(
                from.getId(),
                from.getAmount(),
                productReadDto,
                qualityReadDto,
                productUnitReadDto,
                from.getPrice(),
                from.getSum());
    }
}
