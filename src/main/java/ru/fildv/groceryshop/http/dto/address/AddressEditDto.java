package ru.fildv.groceryshop.http.dto.address;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class AddressEditDto {
    @Size(max = 32, message = "{region.Size}")
    String region;

    @Size(max = 32, message = "{city.Size}")
    String city;

    @Size(max = 64, message = "{street.Size}")
    String street;

    @Size(max = 8, message = "{house.Size}")
    String house;

    @Size(max = 6, message = "{postalCode.Size}")
    String postalCode;

    String description;
}
