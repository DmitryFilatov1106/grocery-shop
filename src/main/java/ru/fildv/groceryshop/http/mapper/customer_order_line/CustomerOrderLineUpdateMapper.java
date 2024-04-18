package ru.fildv.groceryshop.http.mapper.customer_order_line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.*;
import ru.fildv.groceryshop.database.repository.ProductRepository;
import ru.fildv.groceryshop.database.repository.ProductUnitRepository;
import ru.fildv.groceryshop.database.repository.QualityRepository;
import ru.fildv.groceryshop.database.repository.customer_order.CustomerOrderRepository;
import ru.fildv.groceryshop.http.dto.customer_order_line.CustomerOrderLineEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerOrderLineUpdateMapper implements Mapper<CustomerOrderLineEditDto, CustomerOrderLine> {
    private final ProductRepository productRepository;
    private final QualityRepository qualityRepository;
    private final ProductUnitRepository productUnitRepository;
    private final CustomerOrderRepository customerOrderRepository;

    @Override
    public CustomerOrderLine map(CustomerOrderLineEditDto from, CustomerOrderLine to) {
        copy(from, to);
        return to;
    }

    @Override
    public CustomerOrderLine map(CustomerOrderLineEditDto from) {
        CustomerOrderLine productUnit = new CustomerOrderLine();
        copy(from, productUnit);
        return productUnit;
    }

    private void copy(CustomerOrderLineEditDto from, CustomerOrderLine to) {
        to.setProduct(getProduct(from.getProductId()));
        to.setCustomerOrder(getCustomerOrder(from.getCustomerOrderId()));
        to.setId(from.getId());
        to.setAmount(from.getAmount());
        to.setProductUnit(getProductUnit(from.getProductUnitId()));
        to.setQuality(getQuality(from.getQualityId()));
        to.setPrice(from.getPrice());
        to.setSum(from.getSum());
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

    private CustomerOrder getCustomerOrder(Integer customerOrderId) {
        return Optional.ofNullable(customerOrderId)
                .flatMap(customerOrderRepository::findById)
                .orElse(null);
    }
}
