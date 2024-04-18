package ru.fildv.groceryshop.database.repository.provider_order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.fildv.groceryshop.database.entity.ProviderOrder;
import ru.fildv.groceryshop.database.entity.ProviderOrder_;
import ru.fildv.groceryshop.http.dto.provider_order.ProviderOrderFilterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ProviderOrderFilterRepositoryImpl implements ProviderOrderFilterRepository {
    private final EntityManager entityManager;

    @Override
    public Page<ProviderOrder> findAllByFilter(ProviderOrderFilterDto filter, Pageable pageable) {
        // content
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(ProviderOrder.class);

        var providerOrder = criteria.from(ProviderOrder.class);

        List<Predicate> predicates = getListPredicate(cb, filter, providerOrder);

        criteria.select(providerOrder).where(predicates.toArray(new Predicate[0])).orderBy(cb.asc(providerOrder.get(ProviderOrder_.id)));

        List<ProviderOrder> list = entityManager.createQuery(criteria)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // count
        var criteria2 = cb.createQuery(Long.class);

        var providerOrder2 = criteria2.from(ProviderOrder.class);

        List<Predicate> predicates2 = getListPredicate(cb, filter, providerOrder2);

        criteria2.select(cb.count(providerOrder2)).where(predicates2.toArray(new Predicate[0]));
        var count = entityManager.createQuery(criteria2).getSingleResult();

        return new PageImpl<>(list, pageable, count);

    }

    private List<Predicate> getListPredicate(CriteriaBuilder cb, ProviderOrderFilterDto filter, Root<ProviderOrder> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(filter.getId()))
            predicates.add(cb.equal(root.get(ProviderOrder_.id), filter.getId()));
        if (Objects.nonNull(filter.getFromOrderDate()))
            predicates.add(cb.greaterThanOrEqualTo(root.get(ProviderOrder_.ORDER_DATE), filter.getFromOrderDate()));
        if (Objects.nonNull(filter.getToOrderDate()))
            predicates.add(cb.lessThanOrEqualTo(root.get(ProviderOrder_.ORDER_DATE), filter.getToOrderDate()));
        if (Objects.nonNull(filter.getComment()))
            predicates.add(cb.like(cb.lower(root.get(ProviderOrder_.comment)), "%" + filter.getComment().toLowerCase() + "%"));

        return predicates;
    }

}
