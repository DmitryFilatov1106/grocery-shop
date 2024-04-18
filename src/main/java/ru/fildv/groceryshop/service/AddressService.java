package ru.fildv.groceryshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fildv.groceryshop.database.entity.enums.AddressFor;
import ru.fildv.groceryshop.http.dto.address.AddressEditDto;
import ru.fildv.groceryshop.http.dto.address.AddressInfoDto;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AddressService {
    private final UserService userService;
    private final CustomerService customerService;

    public Optional<AddressInfoDto> findById(Integer id, AddressFor addressFor) {
        switch (addressFor) {
            case USER -> {
                return userService.getAddress(id);
            }
            case CUSTOMER -> {
                return customerService.getAddress(id);
            }
        }
        return Optional.empty();
    }

    @Transactional
    public boolean update(Integer id, AddressFor addressFor, AddressEditDto address) {
        switch (addressFor) {
            case USER -> {
                return userService.setAddress(id, address);
            }
            case CUSTOMER -> {
                return customerService.setAddress(id, address);
            }
        }
        return false;
    }
}
