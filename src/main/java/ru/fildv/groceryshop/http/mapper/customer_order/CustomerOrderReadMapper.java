package ru.fildv.groceryshop.http.mapper.customer_order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.CustomerOrder;
import ru.fildv.groceryshop.http.dto.customer.CustomerReadDto;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderReadDto;
import ru.fildv.groceryshop.http.dto.project.ProjectReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;
import ru.fildv.groceryshop.http.mapper.customer.CustomerReadMapper;
import ru.fildv.groceryshop.http.mapper.project.ProjectReadMapper;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomerOrderReadMapper implements Mapper<CustomerOrder, CustomerOrderReadDto> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final CustomerReadMapper customerReadMapper;
    private final ProjectReadMapper projectReadMapper;

    @Override
    public CustomerOrderReadDto map(CustomerOrder from) {
        CustomerReadDto customer = Optional.ofNullable(from.getCustomer())
                .map(customerReadMapper::map)
                .orElse(null);
        ProjectReadDto project = Optional.ofNullable(from.getProject())
                .map(projectReadMapper::map)
                .orElse(null);

        return new CustomerOrderReadDto(
                from.getId(),
                from.getOrderDate(),
                from.getOrderDate().format(formatter),
                from.getCommit(),
                from.getCommit() ? "v" : "-",
                from.getComment(),
                from.getTotalSum(),
                customer,
                project
        );
    }
}
