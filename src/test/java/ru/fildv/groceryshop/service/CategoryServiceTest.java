package ru.fildv.groceryshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fildv.groceryshop.database.entity.Category;
import ru.fildv.groceryshop.database.repository.CategoryRepository;
import ru.fildv.groceryshop.http.dto.category.CategoryReadDto;
import ru.fildv.groceryshop.http.mapper.category.CategoryReadMapper;
import ru.fildv.groceryshop.http.mapper.category.CategoryUpdateMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static final Integer CATEGORY_ID = 1;
    private static final String CATEGORY_NAME = "Fruit";
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryReadMapper categoryReadMapper;
    @Mock
    private CategoryUpdateMapper categoryUpdateMapper;
    @InjectMocks
    private CategoryService categoryService;


    @Test
    void findById() {
        Category category = new Category(CATEGORY_ID, CATEGORY_NAME);
        CategoryReadDto categoryReadDto = new CategoryReadDto(CATEGORY_ID, CATEGORY_NAME);

        doReturn(Optional.of(category))
                .when(categoryRepository).findById(CATEGORY_ID);

        doReturn(categoryReadDto)
                .when(categoryReadMapper).map(category);

        var actualResult = categoryService.findById(CATEGORY_ID);

        assertTrue(actualResult.isPresent());
        var expectedResult = new CategoryReadDto(CATEGORY_ID, CATEGORY_NAME);
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }
}