package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.entity.CustomerOrderLine;
import ru.fildv.groceryshop.database.repository.CustomerOrderLineRepository;
import ru.fildv.groceryshop.http.dto.customer_order_line.CustomerOrderLineEditDto;
import ru.fildv.groceryshop.http.dto.customer_order_line.CustomerOrderLineReadDto;
import ru.fildv.groceryshop.http.mapper.customer_order_line.CustomerOrderLineReadMapper;
import ru.fildv.groceryshop.http.mapper.customer_order_line.CustomerOrderLineUpdateMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerOrderLineService {
    private final CustomerOrderLineRepository customerOrderLineRepository;
    private final CustomerOrderLineReadMapper customerOrderLineReadMapper;
    private final CustomerOrderLineUpdateMapper customerOrderLineUpdateMapper;
    private final StockService stockService;

    public List<CustomerOrderLineReadDto> findAllByCustomerOrderId(Integer customerOrderId) {
        return customerOrderLineRepository.findAllByOrderId(customerOrderId).stream()
                .map(customerOrderLineReadMapper::map)
                .collect(toList());
    }

    @Transactional
    public boolean delete(Integer id) {
        return customerOrderLineRepository.findById(id)
                .map(entity -> {
                    var sum = entity.getSum();

                    customerOrderLineRepository.delete(entity);
                    customerOrderLineRepository.flush();

                    var order = entity.getCustomerOrder();
                    order.setTotalSum(order.getTotalSum().subtract(sum));

                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public CustomerOrderLineReadDto create(CustomerOrderLineEditDto dto) {
        return Optional.of(dto)
                .map(customerOrderLineUpdateMapper::map)
                .map(entity -> {
                    var purchasePrice = entity.getProduct().getPurchasePrice();
                    var ratio = entity.getProductUnit().getRatio();
                    entity.setPrice(ratio.multiply(purchasePrice));
                    entity.setSum(entity.getPrice().multiply(BigDecimal.valueOf(entity.getAmount())));

                    var order = entity.getCustomerOrder();
                    order.setTotalSum(order.getTotalSum().add(entity.getSum()));

                    return entity;
                })
                .map(customerOrderLineRepository::save)
                .map(customerOrderLineReadMapper::map)
                .orElseThrow();
    }

    public List<Integer> commitLines(Integer customerOrderId) {
        List<Integer> result = new ArrayList<>();
        var lines = customerOrderLineRepository.findAllByOrderId(customerOrderId);
        for (CustomerOrderLine line : lines) {
            var product = line.getProduct();
            var quality = line.getQuality();
            var amount = line.getProductUnit().getRatio().multiply(BigDecimal.valueOf(line.getAmount()));
            var stockAmount = stockService.getAmount(product, quality);

            if (stockAmount.compareTo(amount) < 0) {
                result.add(line.getId());
            } else {
                var oldAmount = product.getStoreAmount();
                product.setStoreAmount(oldAmount.subtract(amount));
                stockService.setDownAmount(product, quality, amount);
            }
        }
        return result;
    }
}
