package kriuchkov.maksim.persist.repo;

import kriuchkov.maksim.persist.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByPriceGreaterThanEqual(BigDecimal minPrice);

    List<Product> findByPriceLessThanEqual(BigDecimal maxPrice);

    List<Product> findByPriceGreaterThanEqualAndPriceLessThanEqual(BigDecimal minPrice, BigDecimal maxPrice);
}
