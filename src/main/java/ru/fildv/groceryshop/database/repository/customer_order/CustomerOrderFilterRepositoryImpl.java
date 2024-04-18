package ru.fildv.groceryshop.database.repository.customer_order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.fildv.groceryshop.database.entity.CustomerOrder;
import ru.fildv.groceryshop.database.entity.CustomerOrder_;
import ru.fildv.groceryshop.http.dto.customer_order.CustomerOrderFilterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomerOrderFilterRepositoryImpl implements CustomerOrderFilterRepository {
    private final EntityManager entityManager;

    @Override
    public Page<CustomerOrder> findAllByFilter(CustomerOrderFilterDto filter, Pageable pageable) {
        // content
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(CustomerOrder.class);

        var customerOrder = criteria.from(CustomerOrder.class);
        customerOrder.fetch(CustomerOrder_.customer, JoinType.LEFT);
        customerOrder.fetch(CustomerOrder_.project, JoinType.LEFT);

        List<Predicate> predicates = getListPredicate(cb, filter, customerOrder);

        criteria.select(customerOrder).where(predicates.toArray(new Predicate[0])).orderBy(cb.asc(customerOrder.get(CustomerOrder_.id)));

        List<CustomerOrder> list = entityManager.createQuery(criteria)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // count
        var criteria2 = cb.createQuery(Long.class);

        var customerOrder2 = criteria2.from(CustomerOrder.class);

        List<Predicate> predicates2 = getListPredicate(cb, filter, customerOrder2);

        criteria2.select(cb.count(customerOrder2)).where(predicates2.toArray(new Predicate[0]));
        var count = entityManager.createQuery(criteria2).getSingleResult();

        return new PageImpl<>(list, pageable, count);

    }

    private List<Predicate> getListPredicate(CriteriaBuilder cb, CustomerOrderFilterDto filter, Root<CustomerOrder> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(filter.getId()))
            predicates.add(cb.equal(root.get(CustomerOrder_.id), filter.getId()));
        if (Objects.nonNull(filter.getFromOrderDate()))
            predicates.add(cb.greaterThanOrEqualTo(root.get(CustomerOrder_.ORDER_DATE), filter.getFromOrderDate()));
        if (Objects.nonNull(filter.getToOrderDate()))
            predicates.add(cb.lessThanOrEqualTo(root.get(CustomerOrder_.ORDER_DATE), filter.getToOrderDate()));
        if (Objects.nonNull(filter.getComment()))
            predicates.add(cb.like(cb.lower(root.get(CustomerOrder_.comment)), "%" + filter.getComment().toLowerCase() + "%"));

        return predicates;
    }

}
