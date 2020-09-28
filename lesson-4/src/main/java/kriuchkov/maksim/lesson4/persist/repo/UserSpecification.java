package kriuchkov.maksim.lesson4.persist.repo;

import kriuchkov.maksim.lesson4.persist.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class UserSpecification {

    public static Specification<Product> trueLiteral() {
        return (root, query, builder) -> builder.isTrue(builder.literal(true));
    }

    public static Specification<Product> priceGreaterOrEqual(BigDecimal priceMin) {
        return (root, query, builder) -> builder.ge(root.get("price"), priceMin);
    }

    public static Specification<Product> priceLessOrEqual(BigDecimal priceMax) {
        return (root, query, builder) -> builder.le(root.get("price"), priceMax);
    }
}
