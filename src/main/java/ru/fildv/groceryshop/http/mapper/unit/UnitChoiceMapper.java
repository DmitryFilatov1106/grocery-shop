package ru.fildv.groceryshop.http.mapper.unit;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Unit;
import ru.fildv.groceryshop.http.dto.unit.UnitChoiceDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class UnitChoiceMapper implements Mapper<Unit, UnitChoiceDto> {
    @Override
    public UnitChoiceDto map(Unit from) {
        return new UnitChoiceDto(
                from.getId(),
                from.getShortName());
    }
}
