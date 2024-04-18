package ru.fildv.groceryshop.http.dto.customer_order;

import lombok.Value;
import ru.fildv.groceryshop.http.dto.customer.CustomerReadDto;
import ru.fildv.groceryshop.http.dto.project.ProjectReadDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class CustomerOrderReadDto {
    Integer id;

    LocalDate orderDate;
    String sOrderDate;

    Boolean commit;
    String sCommit;

    String comment;
    BigDecimal totalSum;
    CustomerReadDto customer;
    ProjectReadDto project;
}
