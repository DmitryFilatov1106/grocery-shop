package ru.fildv.groceryshop.database.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.fildv.groceryshop.database.entity.enums.Role;
import ru.fildv.groceryshop.database.entity.user.User;
import ru.fildv.groceryshop.database.entity.user.User_;
import ru.fildv.groceryshop.http.dto.user.UserFilterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class UserFilterRepositoryImpl implements UserFilterRepository {
    private final EntityManager entityManager;

    @Override
    public Page<User> findAllByFilter(UserFilterDto filter, Pageable pageable) {
        // content
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);

        var user = criteria.from(User.class);

        List<Predicate> predicates = getListPredicate(cb, filter, user);

        criteria.select(user).where(predicates.toArray(new Predicate[0])).orderBy(cb.asc(user.get(User_.id)));

        List<User> list = entityManager.createQuery(criteria)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // count
        var criteria2 = cb.createQuery(Long.class);

        var user2 = criteria2.from(User.class);

        List<Predicate> predicates2 = getListPredicate(cb, filter, user2);

        criteria2.select(cb.count(user2)).where(predicates2.toArray(new Predicate[0]));
        var count = entityManager.createQuery(criteria2).getSingleResult();

        return new PageImpl<>(list, pageable, count);
    }

    private List<Predicate> getListPredicate(CriteriaBuilder cb, UserFilterDto filter, Root<User> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(filter.getUsername()))
            predicates.add(cb.like(cb.lower(root.get(User_.username)), "%" + filter.getUsername().toLowerCase() + "%"));
        if (Objects.nonNull(filter.getFirstname()))
            predicates.add(cb.like(cb.lower(root.get(User_.firstname)), "%" + filter.getFirstname().toLowerCase() + "%"));
        if (Objects.nonNull(filter.getLastname()))
            predicates.add(cb.like(cb.lower(root.get(User_.lastname)), "%" + filter.getLastname().toLowerCase() + "%"));
        if (!(Objects.isNull(filter.getRole()) || filter.getRole().equals("")))
            predicates.add(cb.equal(root.get(User_.role), Role.valueOf(filter.getRole())));
        return predicates;
    }
}
