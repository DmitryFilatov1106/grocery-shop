package ru.fildv.groceryshop.http.mapper.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.user.User;
import ru.fildv.groceryshop.http.dto.address.AddressReadDto;
import ru.fildv.groceryshop.http.dto.user.UserReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;
import ru.fildv.groceryshop.http.mapper.address.AddressReadMapper;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {
    private final AddressReadMapper addressReadMapper;

    @Override
    public UserReadDto map(User from) {
        AddressReadDto address = Optional.ofNullable(from.getAddress())
                .map(addressReadMapper::map)
                .orElse(null);

        return new UserReadDto(
                from.getId(),
                from.getUsername(),
                from.getFirstname(),
                from.getLastname(),
                from.getBirthDay(),
                from.getRole().name(),
                address,
                from.getImage()
        );
    }
}
