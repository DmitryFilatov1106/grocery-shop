package ru.fildv.groceryshop.http.mapper.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Customer;
import ru.fildv.groceryshop.http.dto.customer.CustomerSimpleReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@RequiredArgsConstructor
@Component
public class CustomerSimpleReadMapper implements Mapper<Customer, CustomerSimpleReadDto> {

    @Override
    public CustomerSimpleReadDto map(Customer from) {
        return new CustomerSimpleReadDto(
                from.getId(),
                from.getName(),
                from.getStatus().getName(),
                from.getManager().getLastname() + " " + from.getManager().getFirstname()
        );
    }
}
