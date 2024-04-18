package ru.fildv.groceryshop.http.mapper.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Customer;
import ru.fildv.groceryshop.database.entity.user.User;
import ru.fildv.groceryshop.http.dto.customer.CustomerEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomerEditMapper implements Mapper<Customer, CustomerEditDto> {

    @Override
    public CustomerEditDto map(Customer from) {
        Integer managerId = Optional.ofNullable(from.getManager())
                .map(User::getId)
                .orElse(null);

        return new CustomerEditDto(
                from.getId(),
                from.getName(),
                from.getContract(),
                from.getCustomerSince(),
                from.getStatus().getName(),
                managerId
        );
    }
}
