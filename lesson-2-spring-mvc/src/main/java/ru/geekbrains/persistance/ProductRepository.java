package ru.geekbrains.persistance;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final List<Product> list;

    public ProductRepository() {
        list = new ArrayList<>();
        list.add(new Product(1, "Sample product", new BigDecimal("9.99")));
        list.add(new Product(2, "Sample product", new BigDecimal("99.99")));
    }

    public void insert(Product product) {
        if (list.size() == 0)
            product.setId(1);
        else
            product.setId(list.get(list.size() - 1).getId() + 1);
        list.add(product);
    }

    public void update(Product product) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == product.getId()) {
                list.set(i, product);
                break;
            }
        }
    }

    public Optional<Product> findById(Long id) {
        for (Product p : list) {
            if (p.getId() == id) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(list);
    }
}
