package ru.fildv.groceryshop.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.database.entity.ProductUnit;
import ru.fildv.groceryshop.database.entity.Unit;

import java.util.List;
import java.util.Optional;

public interface ProductUnitRepository extends JpaRepository<ProductUnit, Integer> {
    @Query("from ProductUnit pu join fetch pu.product p join fetch pu.product.category join fetch pu.unit where p.id = :productId")
    List<ProductUnit> findAllByProductIdWithProductCategoryUnit(@Param("productId") Integer productId);

    Optional<ProductUnit> findFirstByProductAndUnit(Product product, Unit unit);
}
