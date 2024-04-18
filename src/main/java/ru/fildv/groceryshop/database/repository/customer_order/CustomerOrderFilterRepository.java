package ru.fildv.groceryshop.database.repository.customer_order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.fildv.groceryshop.database.entity.CustomerOrder;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderFilterDto;

public interface CustomerOrderFilterRepository {
    Page<CustomerOrder> findAllByFilter(CustomerOrderFilterDto filter, Pageable pageable);
}
