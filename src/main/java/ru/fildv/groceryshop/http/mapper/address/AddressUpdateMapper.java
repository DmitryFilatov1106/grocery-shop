package ru.fildv.groceryshop.http.mapper.address;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Address;
import ru.fildv.groceryshop.http.dto.address.AddressEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class AddressUpdateMapper implements Mapper<AddressEditDto, Address> {
    @Override
    public Address map(AddressEditDto from) {
        Address address = new Address();
        copy(from, address);
        return address;
    }

    @Override
    public Address map(AddressEditDto from, Address to) {
        copy(from, to);
        return to;
    }

    private void copy(AddressEditDto from, Address to) {
        to.setRegion(from.getRegion());
        to.setCity(from.getCity());
        to.setStreet(from.getStreet());
        to.setHouse(from.getHouse());
        to.setPostalCode(from.getPostalCode());
    }
}
