package kriuchkov.maksim.controller;

import kriuchkov.maksim.persist.entity.Product;
import kriuchkov.maksim.persist.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @GetMapping
    public String getProducts(Model model,
                              @RequestParam(value = "priceMin", required = false) BigDecimal priceMin,
                              @RequestParam(value = "priceMax", required = false) BigDecimal priceMax) {
        List<Product> list;

        if (priceMin == null && priceMax == null)
            list = repository.findAll();
        else if (priceMin != null && priceMax == null)
            list = repository.findByPriceGreaterThanEqual(priceMin);
        else if (priceMin == null)
            list = repository.findByPriceLessThanEqual(priceMax);
        else
            list = repository.findByPriceGreaterThanEqualAndPriceLessThanEqual(priceMin, priceMax);

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
        else
            repository.save(product);
        return "redirect:/product";
    }

}
