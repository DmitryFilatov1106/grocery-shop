package ru.fildv.groceryshop.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fildv.groceryshop.database.entity.Product;
import ru.fildv.groceryshop.database.entity.Quality;
import ru.fildv.groceryshop.database.entity.Stock;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<Stock> findStockByProductAndQuality(Product product, Quality quality);

    @Query(value = "from Stock s join fetch s.product join fetch s.product.baseUnit join fetch s.quality where s.quality.id = :idQuality and s.product.id = :idProduct",
            countQuery = "from Stock s where s.quality.id = :idQuality and s.product.id = :idProduct")
    Page<Stock> findAllByProductIdAndQualityId(@Param("idProduct") Integer product, @Param("idQuality") Integer quality, Pageable pageable);

    @Query(value = "from Stock s join fetch s.product join fetch s.product.baseUnit join fetch s.quality where s.product.id = :idProduct",
            countQuery = "from Stock s where s.product.id = :idProduct")
    Page<Stock> findAllByProductId(@Param("idProduct") Integer product, Pageable pageable);

    @Query(value = "from Stock s join fetch s.product join fetch s.product.baseUnit join fetch s.quality where s.quality.id = :idQuality",
            countQuery = "from Stock s where s.quality.id = :idQuality")
    Page<Stock> findAllByQualityId(@Param("idQuality") Integer quality, Pageable pageable);

    @EntityGraph(attributePaths = {"product", "quality", "product.baseUnit"})
    Page<Stock> findAllBy(Pageable pageable);
}
