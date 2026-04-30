package api.gymmanagement.repositories;

import api.gymmanagement.entities.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
List<MembershipPlan> findByGymId(Long gymId);
}
