package ru.fildv.groceryshop.http.mapper.customer_order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Customer;
import ru.fildv.groceryshop.database.entity.CustomerOrder;
import ru.fildv.groceryshop.database.entity.Project;
import ru.fildv.groceryshop.database.repository.ProjectRepository;
import ru.fildv.groceryshop.database.repository.customer.CustomerRepository;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerOrderUpdateMapper implements Mapper<CustomerOrderEditDto, CustomerOrder> {
    private final CustomerRepository customerRepository;
    private final ProjectRepository projectRepository;

    @Override
    public CustomerOrder map(CustomerOrderEditDto from, CustomerOrder to) {
        copy(from, to);
        return to;
    }

    @Override
    public CustomerOrder map(CustomerOrderEditDto from) {
        CustomerOrder customerOrder = new CustomerOrder();
        copy(from, customerOrder);
        return customerOrder;
    }

    private void copy(CustomerOrderEditDto from, CustomerOrder to) {
        to.setOrderDate(from.getOrderDate());
        to.setCustomer(getCustomer(from.getCustomerId()));
        to.setProject(getProject(from.getProjectId()));
        to.setCommit(Objects.isNull(from.getCommit()) ? false : from.getCommit());
        to.setComment(from.getComment());
        to.setTotalSum(Objects.isNull(from.getTotalSum()) ? BigDecimal.ZERO : from.getTotalSum());
    }

    public Customer getCustomer(Integer customerId) {
        return Optional.ofNullable(customerId)
                .flatMap(customerRepository::findById)
                .orElse(null);
    }

    public Project getProject(Integer projectId) {
        return Optional.ofNullable(projectId)
                .flatMap(projectRepository::findById)
                .orElse(null);
    }
}
