package ru.fildv.groceryshop.http.dto.user;

import lombok.Value;
import ru.fildv.groceryshop.http.dto.address.AddressReadDto;

import java.time.LocalDate;

@Value
public class UserReadDto {
    Integer id;
    String username;
    String firstname;
    String lastname;
    LocalDate birthDay;
    String role;
    AddressReadDto address;
    String image;
}
