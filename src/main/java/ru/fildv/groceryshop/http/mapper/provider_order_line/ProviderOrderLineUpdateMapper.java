package ru.fildv.groceryshop.http.mapper.provider_order_line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.*;
import ru.fildv.groceryshop.database.repository.ProductRepository;
import ru.fildv.groceryshop.database.repository.ProductUnitRepository;
import ru.fildv.groceryshop.database.repository.QualityRepository;
import ru.fildv.groceryshop.database.repository.provider_order.ProviderOrderRepository;
import ru.fildv.groceryshop.http.dto.provider_order_line.ProviderOrderLineEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProviderOrderLineUpdateMapper implements Mapper<ProviderOrderLineEditDto, ProviderOrderLine> {
    private final ProductRepository productRepository;
    private final QualityRepository qualityRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ProviderOrderRepository providerOrderRepository;

    @Override
    public ProviderOrderLine map(ProviderOrderLineEditDto from, ProviderOrderLine to) {
        copy(from, to);
        return to;
    }

    @Override
    public ProviderOrderLine map(ProviderOrderLineEditDto from) {
        ProviderOrderLine productUnit = new ProviderOrderLine();
        copy(from, productUnit);
        return productUnit;
    }

    private void copy(ProviderOrderLineEditDto from, ProviderOrderLine to) {
        to.setProduct(getProduct(from.getProductId()));
        to.setProviderOrder(getProviderOrder(from.getProviderOrderId()));
        to.setId(from.getId());
        to.setAmount(from.getAmount());
        to.setProductUnit(getProductUnit(from.getProductUnitId()));
        to.setQuality(getQuality(from.getQualityId()));
    }

    private Product getProduct(Integer productId) {
        return Optional.ofNullable(productId)
                .flatMap(productRepository::findById)
                .orElse(null);
    }

    private Quality getQuality(Integer qualityId) {
        return Optional.ofNullable(qualityId)
                .flatMap(qualityRepository::findById)
                .orElse(null);
    }

    private ProductUnit getProductUnit(Integer productUnitId) {
        return Optional.ofNullable(productUnitId)
                .flatMap(productUnitRepository::findById)
                .orElse(null);
    }

    private ProviderOrder getProviderOrder(Integer providerOrderId) {
        return Optional.ofNullable(providerOrderId)
                .flatMap(providerOrderRepository::findById)
                .orElse(null);
    }
}
