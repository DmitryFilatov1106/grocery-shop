package ru.fildv.groceryshop.http.mapper.customer_order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.CustomerOrder;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderSimpleReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
public class CustomerOrderSimpleReadMapper implements Mapper<CustomerOrder, CustomerOrderSimpleReadDto> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public CustomerOrderSimpleReadDto map(CustomerOrder from) {
        var customer = from.getCustomer();
        var project = from.getProject();

        return new CustomerOrderSimpleReadDto(
                from.getId(),
                from.getOrderDate(),
                from.getOrderDate().format(formatter),
                from.getCommit(),
                from.getCommit() ? "v" : "-",
                from.getComment(),
                from.getTotalSum(),
                customer == null ? "-" : customer.getName(),
                project == null ? "-" : project.getName()
        );
    }
}
