package ru.fildv.groceryshop.http.mapper.unit;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Unit;
import ru.fildv.groceryshop.http.dto.unit.UnitReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class UnitReadMapper implements Mapper<Unit, UnitReadDto> {
    @Override
    public UnitReadDto map(Unit from) {
        return new UnitReadDto(
                from.getId(),
                from.getName(),
                from.getShortName());
    }
}
