package kriuchkov.maksim.lesson4.persist.repo;

import kriuchkov.maksim.lesson4.persist.entity.User;
import kriuchkov.maksim.lesson4.persist.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>, JpaSpecificationExecutor<UserRole> {

}
