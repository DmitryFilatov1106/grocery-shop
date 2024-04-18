package ru.fildv.groceryshop.http.mapper.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Category;
import ru.fildv.groceryshop.http.dto.category.CategoryEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

@Component
@RequiredArgsConstructor
public class CategoryUpdateMapper implements Mapper<CategoryEditDto, Category> {

    @Override
    public Category map(CategoryEditDto from, Category to) {
        copy(from, to);
        return to;
    }

    @Override
    public Category map(CategoryEditDto from) {
        Category category = new Category();
        copy(from, category);
        return category;
    }

    private void copy(CategoryEditDto from, Category to) {
        to.setName(from.getName());
    }
}
