package ru.fildv.groceryshop.http.dto.customer_order;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class CustomerOrderSimpleReadDto {
    Integer id;

    LocalDate orderDate;
    String sOrderDate;

    Boolean commit;
    String sCommit;

    String comment;
    BigDecimal totalSum;
    String customer;
    String project;
}
