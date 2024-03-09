package suprun.bookstore.repository.role;

import java.util.Optional;
import suprun.bookstore.model.Role;
import suprun.bookstore.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
