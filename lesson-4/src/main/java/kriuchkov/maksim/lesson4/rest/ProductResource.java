package kriuchkov.maksim.lesson4.rest;

import javassist.NotFoundException;
import kriuchkov.maksim.lesson4.persist.entity.Product;
import kriuchkov.maksim.lesson4.persist.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@RequestMapping("/api/v1/product")
@RestController
public class ProductResource {

    private final ProductRepository repository;

    @Autowired
    public ProductResource(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/all", produces = "application/json")
    public List<Product> findAll() {
        return repository.findAll();
    }

    @GetMapping(path = "/{id}/id", produces = "application/json")
    public Product findById(@PathVariable Integer id) {
        Optional<Product> p = repository.findById(id);
        if (p.isPresent())
            return p.get();
        else
            throw new ResponseException(HttpStatus.NOT_FOUND, "No such product with id = " + id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Product createProduct(@RequestBody Product product) {
        if (product.getId() != null) {
            throw new ResponseException(HttpStatus.BAD_REQUEST, "New product ID must be null");
        }

        repository.save(product);
        return product;
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public Product updateProduct(@RequestBody Product product) {
        repository.save(product);
        return product;
    }

    @DeleteMapping(path = "/{id}/id", produces = "application/json")
    public void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    @ExceptionHandler
    public ResponseEntity<String> responseExceptionHandler(ResponseException e) {
        return new ResponseEntity<>(e.getReason(), e.getStatus());
    }


}
