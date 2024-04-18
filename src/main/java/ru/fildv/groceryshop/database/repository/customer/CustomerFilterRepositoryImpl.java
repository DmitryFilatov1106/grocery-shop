package ru.fildv.groceryshop.database.repository.customer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.fildv.groceryshop.database.entity.Customer;
import ru.fildv.groceryshop.database.entity.Customer_;
import ru.fildv.groceryshop.database.entity.enums.Status;
import ru.fildv.groceryshop.http.dto.customer.CustomerFilterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomerFilterRepositoryImpl implements CustomerFilterRepository {

    private final EntityManager entityManager;

    @Override
    public Page<Customer> findAllByFilter(CustomerFilterDto filter, Pageable pageable) {
        // content
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(Customer.class);

        var customer = criteria.from(Customer.class);
        customer.fetch(Customer_.manager, JoinType.LEFT);

        List<Predicate> predicates = getListPredicate(cb, filter, customer);

        criteria.select(customer).where(predicates.toArray(new Predicate[0])).orderBy(cb.asc(customer.get(Customer_.id)));

        List<Customer> list = entityManager.createQuery(criteria)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // count
        var criteria2 = cb.createQuery(Long.class);

        var customer2 = criteria2.from(Customer.class);

        List<Predicate> predicates2 = getListPredicate(cb, filter, customer2);

        criteria2.select(cb.count(customer2)).where(predicates2.toArray(new Predicate[0]));
        var count = entityManager.createQuery(criteria2).getSingleResult();

        return new PageImpl<>(list, pageable, count);
    }

    private List<Predicate> getListPredicate(CriteriaBuilder cb, CustomerFilterDto filter, Root<Customer> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(filter.getName()))
            predicates.add(cb.like(cb.lower(root.get(Customer_.name)), "%" + filter.getName().toLowerCase() + "%"));
        if (!(Objects.isNull(filter.getStatus()) || filter.getStatus().equals("-")))
            predicates.add(cb.equal(root.get(Customer_.status), Status.getStatus(filter.getStatus())));
        return predicates;
    }
}

