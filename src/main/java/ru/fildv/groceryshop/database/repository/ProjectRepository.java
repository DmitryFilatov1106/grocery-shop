package ru.fildv.groceryshop.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fildv.groceryshop.database.entity.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @EntityGraph(attributePaths = {"proManager"})
    Page<Project> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = {"proManager"})
    List<Project> findAllBy();

    @EntityGraph(attributePaths = {"proManager"})
    Optional<Project> findFirstById(Integer id);

    @EntityGraph(attributePaths = {"proManager"})
    Page<Project> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("from Project p join fetch p.proManager where p.id = :id")
    Optional<Project> findByIdCustom(@Param("id") Integer projectId);
}
