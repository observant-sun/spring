package kriuchkov.maksim.controller;

import kriuchkov.maksim.persist.entity.Product;
import kriuchkov.maksim.persist.repo.ProductRepository;
import kriuchkov.maksim.persist.repo.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository repository;

//  для варианта с CriteriaBuilder

//    @Autowired
//    private EntityManagerFactory entityManagerFactory;
//
//    private EntityManager em;
//
//    @PostConstruct
//    public void init() {
//        em = entityManagerFactory.createEntityManager();
//    }

    @GetMapping
    public String getProducts(Model model,
                              @RequestParam(value = "priceMin", required = false) BigDecimal priceMin,
                              @RequestParam(value = "priceMax", required = false) BigDecimal priceMax) {


//  вариант с CriteriaBuilder

//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Product> query = cb.createQuery(Product.class);
//        Root<Product> from = query.from(Product.class);
//
//        List<Predicate> predicates = new ArrayList<>();
//        if (priceMin != null)
//            predicates.add(cb.ge(from.get("price"), priceMin));
//        if (priceMax != null)
//            predicates.add(cb.le(from.get("price"), priceMax));
//
//        CriteriaQuery<Product> cq = query.select(from).where(predicates.toArray(new Predicate[0]));
//        List<Product> list = em.createQuery(cq).getResultList();

        Specification<Product> spec = UserSpecification.trueLiteral();
        if (priceMin != null)
            spec = spec.and(UserSpecification.priceGreaterOrEqual(priceMin));
        if (priceMax != null)
            spec = spec.and(UserSpecification.priceLessOrEqual(priceMax));
        List<Product> list = repository.findAll(spec);

        model.addAttribute("products", list);
        return "products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product(-1, "", null));
        return "product";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable("id") Integer id, Model model) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "product";
        } else {
            return "product"; // TODO: вывести HTTP 404
        }
    }

    @PostMapping("/update")
    public String updateProduct(Product product) {
        if (repository.findById(product.getId()).isPresent())
            repository.save(product);
        // TODO: else error?
        return "redirect:/product";
    }

}
