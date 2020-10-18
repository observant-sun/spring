package kriuchkov.maksim.lesson4.persist.entity;

import kriuchkov.maksim.lesson4.persist.repo.UserRoleRepository;
import org.springframework.context.annotation.Bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class UserRole {

    @Id
    private Integer id;

    @Column
    private String name;

    public UserRole() {
    }

    public UserRole(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
