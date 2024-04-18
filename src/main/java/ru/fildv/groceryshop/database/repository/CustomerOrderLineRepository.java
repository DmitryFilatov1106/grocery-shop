package ru.fildv.groceryshop.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fildv.groceryshop.database.entity.CustomerOrderLine;

import java.util.List;

public interface CustomerOrderLineRepository extends JpaRepository<CustomerOrderLine, Integer> {
    @Query("from CustomerOrderLine ol " +
            "join fetch ol.product " +
            "join fetch ol.product.category " +
            "join fetch ol.product.baseUnit " +
            "join fetch ol.productUnit pu " +
            "join fetch ol.quality where ol.customerOrder.id = :orderId order by ol.id")
    List<CustomerOrderLine> findAllByOrderId(@Param("orderId") Integer orderId);

//    @Query("select ol from CustomerOrderLine ol join fetch ol.product join fetch ol.customerOrder where ol.id = :id")
//    Optional<CustomerOrderLine> findByIdWithOrder(@Param("id") Integer id);
}
