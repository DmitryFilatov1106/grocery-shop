package ru.fildv.groceryshop.http.mapper.quality;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Quality;
import ru.fildv.groceryshop.http.dto.quality.QualityEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
@RequiredArgsConstructor
public class QualityUpdateMapper implements Mapper<QualityEditDto, Quality> {

    @Override
    public Quality map(QualityEditDto from, Quality to) {
        copy(from, to);
        return to;
    }

    @Override
    public Quality map(QualityEditDto from) {
        Quality quality = new Quality();
        copy(from, quality);
        return quality;
    }

    private void copy(QualityEditDto from, Quality to) {
        to.setName(from.getName());
    }
}
