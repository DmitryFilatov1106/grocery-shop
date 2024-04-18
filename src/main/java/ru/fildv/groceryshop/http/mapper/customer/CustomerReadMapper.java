package ru.fildv.groceryshop.http.mapper.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Customer;
import ru.fildv.groceryshop.http.dto.address.AddressReadDto;
import ru.fildv.groceryshop.http.dto.customer.CustomerReadDto;
import ru.fildv.groceryshop.http.dto.manager.ManagerReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;
import ru.fildv.groceryshop.http.mapper.address.AddressReadMapper;
import ru.fildv.groceryshop.http.mapper.manager.ManagerReadMapper;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomerReadMapper implements Mapper<Customer, CustomerReadDto> {
    private final ManagerReadMapper managerReadMapper;
    private final AddressReadMapper addressReadMapper;

    @Override
    public CustomerReadDto map(Customer from) {
        ManagerReadDto manager = Optional.ofNullable(from.getManager())
                .map(managerReadMapper::map)
                .orElse(null);

        AddressReadDto address = Optional.ofNullable(from.getAddress())
                .map(addressReadMapper::map)
                .orElse(null);

        return new CustomerReadDto(
                from.getId(),
                from.getName(),
                from.getContract(),
                from.getCustomerSince(),
                from.getStatus().getName(),
                manager,
                address
        );
    }
}
