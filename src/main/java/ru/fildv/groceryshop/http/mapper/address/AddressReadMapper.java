package ru.fildv.groceryshop.http.mapper.address;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Address;
import ru.fildv.groceryshop.http.dto.address.AddressReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class AddressReadMapper implements Mapper<Address, AddressReadDto> {
    @Override
    public AddressReadDto map(Address from) {
        return new AddressReadDto(
                from.getRegion(),
                from.getCity(),
                from.getStreet(),
                from.getHouse(),
                from.getPostalCode()
        );
    }
}
