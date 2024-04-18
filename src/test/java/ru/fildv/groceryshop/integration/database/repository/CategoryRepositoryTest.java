package ru.fildv.groceryshop.integration.database.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.fildv.groceryshop.database.entity.Category;
import ru.fildv.groceryshop.database.repository.CategoryRepository;
import ru.fildv.groceryshop.integration.annotation.IT;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@IT
class CategoryRepositoryTest {
//    @LocalServerPort
//    private Integer port;

    @Container
    @ServiceConnection
    public final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        //RestAssured.baseURI = "http://localhost:" + port;
    }

    private static final Integer CATEGORY_ID_2 = 2;
    private static final String CATEGORY_NAME_2 = "Milk";
    private static final String CATEGORY_NAME_CREATE = "New Category";
    private static final Sort SORT = Sort.by(Sort.Direction.ASC, "id");
    private static final int PAGE = 0;
    private static final int SIZEPAGE = 5;
    private static final String CATEGORY_NAME_PART = "IL";

    private final CategoryRepository categoryRepository;


    @Test
    void findById() {
        var category = categoryRepository.findById(CATEGORY_ID_2).get(); //.find(Category.class, CATEGORY_ID_2);
        assertNotNull(category);
        assertThat(category.getName()).isEqualTo(CATEGORY_NAME_2);
    }

    @Test
    void findAllByNameContainingIgnoreCase() {
        var actualResult = categoryRepository.findAllByNameContainingIgnoreCase(CATEGORY_NAME_PART, PageRequest.of(PAGE, SIZEPAGE, SORT));
        assertEquals(1, actualResult.getTotalPages());
        assertEquals(1, actualResult.getContent().size());
        assertEquals(CATEGORY_NAME_2, actualResult.getContent().get(0).getName());
    }

    @Test
    void findAll() {
        Category expectedCategory = new Category(CATEGORY_ID_2, CATEGORY_NAME_2);
        var actualResult = categoryRepository.findAll();

        assertEquals(3, actualResult.size());

        var actualCount = actualResult.stream()
                .filter(e -> e.getId().equals(CATEGORY_ID_2))
                .count();
        assertEquals(1, actualCount);

        var actualCategory = actualResult.stream()
                .filter(e -> e.getId().equals(CATEGORY_ID_2))
                .findAny();
        assertTrue(actualCategory.isPresent());
        actualCategory.ifPresent(actual -> assertEquals(expectedCategory, actual));
    }

    @Test
    void findAllWithPage() {
        var actualResult = categoryRepository.findAll(PageRequest.of(PAGE, SIZEPAGE, SORT));
        assertEquals(1, actualResult.getTotalPages());
        assertEquals(3, actualResult.getContent().size());
    }

    @Test
    void save() {
        var category = Category.builder()
                .name(CATEGORY_NAME_CREATE)
                .build();
        var actualCategory = categoryRepository.saveAndFlush(category);
        assertNotNull(actualCategory.getId());
        assertEquals(CATEGORY_NAME_CREATE, actualCategory.getName());
    }

    @Test
    void delete() {
        var category = Category.builder()
                .id(CATEGORY_ID_2)
                .name(CATEGORY_NAME_2)
                .build();
        assertEquals(3, categoryRepository.findAll().size());
        categoryRepository.delete(category);
        assertEquals(2, categoryRepository.findAll().size());
    }
}