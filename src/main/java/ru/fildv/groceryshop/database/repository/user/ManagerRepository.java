package ru.fildv.groceryshop.database.repository.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.fildv.groceryshop.database.entity.user.Manager;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, Integer> {

}
