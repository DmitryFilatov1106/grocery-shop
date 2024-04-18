package ru.fildv.groceryshop.database.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.fildv.groceryshop.database.entity.enums.Role;
import ru.fildv.groceryshop.database.entity.user.User;
import ru.fildv.groceryshop.http.dto.address.AddressInfoDto;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserFilterRepository {

    Optional<User> findByUsername(String username);

    Optional<User> findFirstByRole(Role role);

    @Query(value = "SELECT region, city, street, house, postal_code postalCode, lastname||' '||firstname description FROM users WHERE id = :id",
            nativeQuery = true)
    Optional<AddressInfoDto> findAddressById(Integer id);
}
