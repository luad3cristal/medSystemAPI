package ifba.edu.br.medSystemAPI.repositories;

import ifba.edu.br.medSystemAPI.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  UserDetails findByEmail(String email);
  Boolean existsByEmail(String email);
  List<User> findByEnabledFalse();
}
