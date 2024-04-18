package ru.fildv.groceryshop.database.repository.customer;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fildv.groceryshop.database.entity.Customer;
import ru.fildv.groceryshop.http.dto.address.AddressInfoDto;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer>, CustomerFilterRepository {
    @Query(value = "SELECT region, city, street, house, postal_code postalCode, name||' ('||contract||')' description FROM customer WHERE id = :id",
            nativeQuery = true)
    Optional<AddressInfoDto> findAddressById(Integer id);


    @Query("from Customer c join fetch c.manager where c.id = :id")
    Optional<Customer> findByIdCustom(@Param("id") Integer customerId);

    @EntityGraph(attributePaths = {"manager"})
    List<Customer> findAll();

    @EntityGraph(attributePaths = {"manager"})
    Optional<Customer> findById(Integer id);
}
