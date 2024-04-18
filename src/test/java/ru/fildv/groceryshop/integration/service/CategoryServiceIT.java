package ru.fildv.groceryshop.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.fildv.groceryshop.http.dto.category.CategoryEditDto;
import ru.fildv.groceryshop.http.dto.category.CategoryFilterDto;
import ru.fildv.groceryshop.http.dto.category.CategoryReadDto;
import ru.fildv.groceryshop.integration.annotation.IT;
import ru.fildv.groceryshop.service.CategoryService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@IT
public class CategoryServiceIT {
//    @LocalServerPort
//    private Integer port;

    @Container
    @ServiceConnection
    public final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private static final Integer CATEGORY_ID_1 = 1;
    private static final Integer CATEGORY_ID_2 = 2;
    private static final String CATEGORY_NAME_2 = "Milk";
    private static final String CATEGORY_NAME_CREATE = "Milk CREATE";
    private static final String CATEGORY_NAME_UPDATE = "Milk UPDATE";

    private final CategoryService categoryService;

    @Test
    void findAllWithPage() {
        CategoryFilterDto dto = new CategoryFilterDto(CATEGORY_NAME_2);
        int page = 0;

        var actualResult = categoryService.findAll(dto, page);
        var actualTotalPages = actualResult.getTotalPages();
        var actualList = actualResult.getContent();
        var actualName = actualList.get(0).getName();

        assertThat(actualList.size(), is(1));

        var expectedResult = new CategoryReadDto(CATEGORY_ID_2, CATEGORY_NAME_2);
        assertThat(actualList, hasItem(expectedResult));

        assertEquals(1, actualTotalPages);
        assertEquals(CATEGORY_NAME_2, actualName);
    }

    @Test
    void findAll() {
        var actualResult = categoryService.findAll();
        assertThat(actualResult.size(), is(3));

        var expectedResult = new CategoryReadDto(CATEGORY_ID_2, CATEGORY_NAME_2);
        assertThat(actualResult, hasItem(expectedResult));
    }

    @Test
    void findAllFilter() {
        var actualResult = categoryService.findAllFilter();
        assertThat(actualResult.size(), is(4));

        var dto = new CategoryReadDto(null, "");
        assertThat(actualResult.get(0), is(dto));
    }

    @Test
    void findById() {
        var actualResult = categoryService.findById(CATEGORY_ID_2);

        assertTrue(actualResult.isPresent());
        var expectedResult = new CategoryReadDto(CATEGORY_ID_2, CATEGORY_NAME_2);
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }

    @Test
    void update() {
        CategoryEditDto categoryDto = new CategoryEditDto(CATEGORY_ID_2, CATEGORY_NAME_UPDATE);

        var actualResult = categoryService.update(CATEGORY_ID_2, categoryDto);

        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertAll(
                () -> assertEquals(CATEGORY_ID_2, actual.getId()),
                () -> assertEquals(CATEGORY_NAME_UPDATE, actual.getName())
        ));
    }

    @Test
    void delete() {
        var actualResultTrue = categoryService.delete(CATEGORY_ID_1);
        var actualResultFalse = categoryService.delete(Integer.MAX_VALUE);

        assertAll(
                () -> assertTrue(actualResultTrue),
                () -> assertFalse(actualResultFalse)
        );
    }

    @Test
    void create() {
        CategoryEditDto categoryDto = new CategoryEditDto(null, CATEGORY_NAME_CREATE);

        var actualResult = categoryService.create(categoryDto);

        assertAll(
                () -> assertNotNull(actualResult.getId()),
                () -> assertEquals(CATEGORY_NAME_CREATE, actualResult.getName())
        );
    }
}
