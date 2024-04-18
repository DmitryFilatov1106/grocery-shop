package ru.fildv.groceryshop.database.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.fildv.groceryshop.database.entity.user.User;
import ru.fildv.groceryshop.http.dto.user.UserFilterDto;

public interface UserFilterRepository {
    Page<User> findAllByFilter(UserFilterDto filter, Pageable pageable);
}
