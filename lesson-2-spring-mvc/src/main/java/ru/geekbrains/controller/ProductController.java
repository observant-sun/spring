package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.persistance.Product;
import ru.geekbrains.persistance.ProductRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @GetMapping
    public String getProducts(Model model) {
        List<Product> list = repository.getAllProducts();
        model.addAttribute("products", list);
        return "products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product(-1, "", null));
        return "product";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable("id") Long id, Model model) {
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
            repository.update(product);
        else
            repository.insert(product);
        return "redirect:/product";
    }

}
