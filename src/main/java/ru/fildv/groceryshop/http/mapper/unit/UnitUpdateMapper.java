package ru.fildv.groceryshop.http.mapper.unit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Unit;
import ru.fildv.groceryshop.http.dto.unit.UnitEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
@RequiredArgsConstructor
public class UnitUpdateMapper implements Mapper<UnitEditDto, Unit> {

    @Override
    public Unit map(UnitEditDto from, Unit to) {
        copy(from, to);
        return to;
    }

    @Override
    public Unit map(UnitEditDto from) {
        Unit unit = new Unit();
        copy(from, unit);
        return unit;
    }

    private void copy(UnitEditDto from, Unit to) {
        to.setName(from.getName());
        to.setShortName(from.getShortName());
    }
}
