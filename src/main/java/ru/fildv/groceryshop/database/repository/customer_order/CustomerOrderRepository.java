package ru.fildv.groceryshop.database.repository.customer_order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.fildv.groceryshop.database.entity.CustomerOrder;

import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer>, CustomerOrderFilterRepository {

    @EntityGraph(attributePaths = {"customer", "customer.manager", "project", "project.proManager"})
    Optional<CustomerOrder> findById(Integer customerOrderId);
}
