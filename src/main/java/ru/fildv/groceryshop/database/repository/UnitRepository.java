package ru.fildv.groceryshop.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fildv.groceryshop.database.entity.Unit;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
    Page<Unit> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = "SELECT u1.* FROM unit u1 EXCEPT SELECT u2.* FROM unit u2 join product_unit pu on u2.id = pu.unit_id where pu.product_id = :productId",
            nativeQuery = true)
    List<Unit> findFreeUnits(@Param("productId") Integer productId);
}
