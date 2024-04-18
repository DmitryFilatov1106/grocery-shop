package ru.fildv.groceryshop.http.dto.address;

public interface AddressInfoDto {
    String getRegion();

    String getCity();

    String getStreet();

    String getHouse();

    String getPostalCode();

    String getDescription();
}
