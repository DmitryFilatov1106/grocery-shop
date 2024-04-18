package ru.fildv.groceryshop.http.mapper.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.user.Manager;
import ru.fildv.groceryshop.http.dto.manager.ManagerReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
@RequiredArgsConstructor
public class ManagerReadMapper implements Mapper<Manager, ManagerReadDto> {
    @Override
    public ManagerReadDto map(Manager from) {
        return new ManagerReadDto(
                from.getId(),
                from.getUsername(),
                from.getFirstname(),
                from.getLastname()
        );
    }
}
