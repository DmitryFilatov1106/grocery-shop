package ru.fildv.groceryshop.http.mapper.promanager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.user.ProManager;
import ru.fildv.groceryshop.http.dto.promanager.ProManagerReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
@RequiredArgsConstructor
public class ProManagerReadMapper implements Mapper<ProManager, ProManagerReadDto> {
    @Override
    public ProManagerReadDto map(ProManager from) {
        return new ProManagerReadDto(
                from.getId(),
                from.getUsername(),
                from.getFirstname(),
                from.getLastname()
        );
    }
}
