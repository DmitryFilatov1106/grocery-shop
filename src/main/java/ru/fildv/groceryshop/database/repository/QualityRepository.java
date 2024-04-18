package ru.fildv.groceryshop.database.repository;

import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import ru.fildv.groceryshop.database.entity.Quality;

@Repository
public interface QualityRepository extends JpaRepository<Quality, Integer> {

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    Page<Quality> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    Page<Quality> findAllBy(Pageable pageable);
}
