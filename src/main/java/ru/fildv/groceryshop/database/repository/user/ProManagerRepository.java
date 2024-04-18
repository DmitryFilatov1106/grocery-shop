package ru.fildv.groceryshop.database.repository.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.fildv.groceryshop.database.entity.user.ProManager;

@Repository
public interface ProManagerRepository extends CrudRepository<ProManager, Integer> {

}
