package kriuchkov.maksim.lesson4.controller;

import kriuchkov.maksim.lesson4.persist.entity.Product;
import kriuchkov.maksim.lesson4.persist.entity.User;
import kriuchkov.maksim.lesson4.persist.repo.UserRepository;
import kriuchkov.maksim.lesson4.persist.repo.UserSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String allUsers(Model model,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "email", required = false) String email,
                           @RequestParam(value = "page") Optional<Integer> page,
                           @RequestParam(value = "size") Optional<Integer> size) {

        PageRequest pageRequest = PageRequest.of(page.orElse(1) - 1, size.orElse(10));

        Specification<User> spec = UserSpecification.trueLiteral();
        if (name != null && !name.isEmpty())
            spec = spec.and(UserSpecification.usernameLike(name));
        if (email != null && !email.isEmpty())
            spec = spec.and(UserSpecification.emailLike(email));

        model.addAttribute("usersPage", repository.findAll(spec, pageRequest));
        return "users";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Integer id, Model model) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "user";
        } else {
            return "user"; // TODO: вывести HTTP 404
        }
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/update")
    public String updateUser(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user";
        }

        // TODO реализовать проверку повторного ввода пароля.
        // TODO Использовать метод bindingResult.rejectValue();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return "redirect:/user";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Integer id) {
        repository.deleteById(id);
        return "redirect:/user";
    }
}
