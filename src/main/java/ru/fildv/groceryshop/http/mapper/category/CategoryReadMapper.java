package ru.fildv.groceryshop.http.mapper.category;

import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Category;
import ru.fildv.groceryshop.http.dto.category.CategoryReadDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
public class CategoryReadMapper implements Mapper<Category, CategoryReadDto> {
    @Override
    public CategoryReadDto map(Category from) {
        return new CategoryReadDto(
                from.getId(),
                from.getName()
        );
    }
}
