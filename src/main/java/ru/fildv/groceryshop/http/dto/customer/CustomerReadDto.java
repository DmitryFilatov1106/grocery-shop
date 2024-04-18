package ru.fildv.groceryshop.http.dto.customer;

import lombok.Value;
import ru.fildv.groceryshop.http.dto.address.AddressReadDto;
import ru.fildv.groceryshop.http.dto.manager.ManagerReadDto;

import java.time.LocalDate;

@Value
public class CustomerReadDto {
    Integer id;
    String name;
    String contract;
    LocalDate customerSince;
    String status;
    ManagerReadDto manager;
    AddressReadDto address;
}
