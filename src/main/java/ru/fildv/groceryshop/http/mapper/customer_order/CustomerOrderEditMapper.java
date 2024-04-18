package ru.fildv.groceryshop.http.mapper.customer_order;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.CustomerOrder;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class CustomerOrderEditMapper implements Mapper<CustomerOrder, CustomerOrderEditDto> {
    @Override
    public CustomerOrderEditDto map(CustomerOrder from) {
        return new CustomerOrderEditDto(
                from.getId(),
                from.getOrderDate(),
                from.getCustomer().getId(),
                from.getProject().getId(),
                from.getComment(),
                from.getCommit(),
                from.getTotalSum()
        );
    }
}
