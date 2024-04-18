package ru.fildv.groceryshop.database.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fildv.groceryshop.database.entity.user.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

}
