package kriuchkov.maksim.lesson4.persist.repo;

import kriuchkov.maksim.lesson4.persist.entity.User;
import org.springframework.data.jpa.domain.Specification;


public final class UserSpecification {

    public static Specification<User> trueLiteral() {
        return (root, query, builder) -> builder.isTrue(builder.literal(true));
    }

    public static Specification<User> usernameLike(String username) {
        return (root, query, builder) -> builder.like(root.get("username"), "%" + username + "%");
    }

    public static Specification<User> emailLike(String email) {
        return (root, query, builder) -> builder.like(root.get("email"), "%" + email + "%");
    }
}
