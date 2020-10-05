package kriuchkov.maksim.lesson4.controller;

import kriuchkov.maksim.lesson4.persist.repo.UserSpecification;
import kriuchkov.maksim.lesson4.persist.entity.Product;
import kriuchkov.maksim.lesson4.persist.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @GetMapping
    public String getProducts(Model model,
                              @RequestParam(value = "priceMin", required = false) BigDecimal priceMin,
                              @RequestParam(value = "priceMax", required = false) BigDecimal priceMax,
                              @RequestParam(value = "page") Optional<Integer> pageNumber,
                              @RequestParam(value = "pageSize") Optional<Integer> pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber.orElse(1) - 1, pageSize.orElse(10), Sort.by("name"));

        Specification<Product> spec = UserSpecification.trueLiteral();
        if (priceMin != null)
            spec = spec.and(UserSpecification.priceGreaterOrEqual(priceMin));
        if (priceMax != null)
            spec = spec.and(UserSpecification.priceLessOrEqual(priceMax));
        Page<Product> page = repository.findAll(spec, pageRequest);

        model.addAttribute("productsPage", page);

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
        repository.save(product);
        return "redirect:/product";
    }

}
