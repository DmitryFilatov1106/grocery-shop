package ru.fildv.groceryshop.http.mapper.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.fildv.groceryshop.database.entity.Customer;
import ru.fildv.groceryshop.database.entity.enums.Status;
import ru.fildv.groceryshop.database.entity.user.Manager;
import ru.fildv.groceryshop.database.repository.user.ManagerRepository;
import ru.fildv.groceryshop.http.dto.customer.CustomerEditDto;
import ru.fildv.groceryshop.http.mapper.Mapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerUpdateMapper implements Mapper<CustomerEditDto, Customer> {
    private final ManagerRepository managerRepository;

    @Override
    public Customer map(CustomerEditDto from, Customer to) {
        copy(from, to);
        return to;
    }

    @Override
    public Customer map(CustomerEditDto from) {
        Customer customer = new Customer();
        copy(from, customer);
        return customer;
    }

    private void copy(CustomerEditDto from, Customer to) {
        to.setName(from.getName());
        to.setCustomerSince(from.getCustomerSince());
        to.setStatus(Status.getStatus(from.getStatus()));
        to.setContract(from.getContract());
        to.setManager(getManager(from.getManagerId()));
    }

    public Manager getManager(Integer managerId) {
        return Optional.ofNullable(managerId)
                .flatMap(managerRepository::findById)
                .orElse(null);
    }
}
