package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.persistance.Product;
import ru.geekbrains.persistance.User;
import ru.geekbrains.persistance.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    public String allUsers(Model model) throws SQLException {
        List<User> allUsers = repository.getAllUsers();
        model.addAttribute("users", allUsers);
        return "users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User(-1, "", ""));
        return "user";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) throws SQLException {
        User user = repository.findById(id);
        if (user.getId() != -1) {
            model.addAttribute("user", user);
            return "user";
        } else {
            return "user"; // TODO: вывести HTTP 404
        }
    }

    @PostMapping("/update")
    public String updateUser(User user) throws SQLException {
        if (repository.userExists(user.getId()))
            repository.update(user);
        else
            repository.insert(user);
        return "redirect:/user";
    }
}
