package ru.fildv.groceryshop.http.mapper.quality;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Quality;
import ru.fildv.groceryshop.http.dto.quality.QualityReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class QualityReadMapper implements Mapper<Quality, QualityReadDto> {
    @Override
    public QualityReadDto map(Quality from) {
        return new QualityReadDto(
                from.getId(),
                from.getName()
        );
    }
}
