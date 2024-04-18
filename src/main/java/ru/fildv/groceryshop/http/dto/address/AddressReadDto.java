package ru.fildv.groceryshop.http.dto.address;

import lombok.Value;

@Value
public class AddressReadDto {
    String region;
    String city;
    String street;
    String house;
    String postalCode;
}
