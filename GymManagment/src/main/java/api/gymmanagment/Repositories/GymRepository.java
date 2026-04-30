package api.gymmanagment.Repositories;
import api.gymmanagment.Entities.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GymRepository extends JpaRepository<Gym, Long>{

    Optional<Gym> findByName(String name);
    boolean existsByName(String name);
}
