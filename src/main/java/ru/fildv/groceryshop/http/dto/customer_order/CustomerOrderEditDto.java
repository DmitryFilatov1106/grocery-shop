package ru.fildv.groceryshop.http.dto.customer_order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class CustomerOrderEditDto {
    Integer id;

    @NotNull(message = "{document.Date}")
    LocalDate orderDate;

    @NotNull(message = "{customer.NotNull}")
    Integer customerId;

    @NotNull(message = "{project.NotNull}")
    Integer projectId;

    @Size(max = 255, message = "{document.comment.Size}")
    String comment;

    Boolean commit;
    BigDecimal totalSum;
}
