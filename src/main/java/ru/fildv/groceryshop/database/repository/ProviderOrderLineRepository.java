package ru.fildv.groceryshop.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fildv.groceryshop.database.entity.ProviderOrderLine;

import java.util.List;

public interface ProviderOrderLineRepository extends JpaRepository<ProviderOrderLine, Integer> {
    @Query("from ProviderOrderLine ol " +
            "join fetch ol.providerOrder " +
            "join fetch ol.product " +
            "join fetch ol.productUnit pu " +
            "join fetch ol.quality " +
            "join fetch ol.product.category " +
            "join fetch ol.product.baseUnit where ol.providerOrder.id = :orderId order by ol.id")
    List<ProviderOrderLine> findAllByOrderId(@Param("orderId") Integer orderId);
}
