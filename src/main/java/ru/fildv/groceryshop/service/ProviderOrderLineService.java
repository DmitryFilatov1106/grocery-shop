package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.entity.ProviderOrderLine;
import ru.fildv.groceryshop.database.repository.ProviderOrderLineRepository;
import ru.fildv.groceryshop.http.dto.provider_order_line.ProviderOrderLineEditDto;
import ru.fildv.groceryshop.http.dto.provider_order_line.ProviderOrderLineReadDto;
import ru.fildv.groceryshop.http.mapper.provider_order_line.ProviderOrderLineReadMapper;
import ru.fildv.groceryshop.http.mapper.provider_order_line.ProviderOrderLineUpdateMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProviderOrderLineService {
    private final ProviderOrderLineRepository providerOrderLineRepository;
    private final ProviderOrderLineReadMapper providerOrderLineReadMapper;
    private final ProviderOrderLineUpdateMapper providerOrderLineUpdateMapper;
    private final StockService stockService;

    public List<ProviderOrderLineReadDto> findAllByProviderOrderId(Integer providerOrderId) {
        return providerOrderLineRepository.findAllByOrderId(providerOrderId).stream()
                .map(providerOrderLineReadMapper::map)
                .collect(toList());
    }

    @Transactional
    public boolean delete(Integer id) {
        return providerOrderLineRepository.findById(id)
                .map(entity -> {
                    providerOrderLineRepository.delete(entity);
                    providerOrderLineRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public ProviderOrderLineReadDto create(ProviderOrderLineEditDto dto) {
        return Optional.of(dto)
                .map(providerOrderLineUpdateMapper::map)
                .map(providerOrderLineRepository::save)
                .map(providerOrderLineReadMapper::map)
                .orElseThrow();
    }

    public void commitLines(Integer providerOrderId) {
        var lines = providerOrderLineRepository.findAllByOrderId(providerOrderId);
        for (ProviderOrderLine line : lines) {
            var amount = line.getProductUnit().getRatio().multiply(BigDecimal.valueOf(line.getAmount()));
            var product = line.getProduct();
            var quality = line.getQuality();
            var oldAmount = product.getStoreAmount();
            var newAmount = oldAmount == null ? amount : oldAmount.add(amount);

            product.setStoreAmount(newAmount);

            stockService.setUpAmount(product, quality, amount);
        }
    }
}
