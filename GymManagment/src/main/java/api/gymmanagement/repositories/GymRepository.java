package api.gymmanagement.repositories;
import api.gymmanagement.entities.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GymRepository extends JpaRepository<Gym, Long>{

    Optional<Gym> findByName(String name);
    boolean existsByName(String name);

    boolean existsByPhone(String phone);
}
