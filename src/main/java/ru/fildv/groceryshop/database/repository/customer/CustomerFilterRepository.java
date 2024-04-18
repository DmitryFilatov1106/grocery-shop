package ru.fildv.groceryshop.database.repository.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.fildv.groceryshop.database.entity.Customer;
import ru.fildv.groceryshop.http.dto.customer.CustomerFilterDto;

public interface CustomerFilterRepository {
    Page<Customer> findAllByFilter(CustomerFilterDto filter, Pageable pageable);
}
