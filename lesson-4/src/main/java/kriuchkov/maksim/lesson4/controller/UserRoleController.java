package kriuchkov.maksim.lesson4.controller;

import kriuchkov.maksim.lesson4.persist.entity.UserRole;
import kriuchkov.maksim.lesson4.persist.repo.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserRoleController {

    @Autowired
    private UserRoleRepository repository;

    @Bean
    public List<UserRole> userRoles() {
        return repository.findAll();
    }
}
