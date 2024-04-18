package ru.fildv.groceryshop.http.dto.address;

import lombok.Value;

@Value
public class AddressViewDto {
    String addressFor;
    AddressReadDto addressDto;
}
