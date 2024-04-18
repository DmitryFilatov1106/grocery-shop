package ru.fildv.groceryshop.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fildv.groceryshop.database.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @EntityGraph(attributePaths = {"baseUnit", "category"})
    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"baseUnit", "category"})
    List<Product> findAllBy();

    @EntityGraph(attributePaths = {"baseUnit", "category"})
    Optional<Product> findFirstById(Integer id);

    @Query(value = "from Product p join fetch p.baseUnit join fetch p.category where p.category.id = :categoryId",
            countQuery = "from Product p where p.category.id = :categoryId")
    Page<Product> findAllByCategoryId(@Param("categoryId") Integer category, Pageable pageable);

    @Query(value = "from Product p join fetch p.baseUnit join fetch p.category where p.category.id = :categoryId and upper(p.name) like %:name%",
            countQuery = "from Product p where p.category.id = :categoryId and upper(p.name) like %:name%")
    Page<Product> findAllByNameContainingIgnoreCaseAndCategoryId(@Param("name") String name, @Param("categoryId") Integer category, Pageable pageable);

    @EntityGraph(attributePaths = {"baseUnit", "category"})
    Page<Product> findAllBy(Pageable pageable);

    @Query("from Product p join fetch p.category join fetch p.baseUnit where p.id = :id")
    Optional<Product> findByIdCustom(@Param("id") Integer productId);

    @Query("from Product p join fetch p.comments where p.id = :id")
    Optional<Product> findComments(@Param("id") Integer productId);
}
