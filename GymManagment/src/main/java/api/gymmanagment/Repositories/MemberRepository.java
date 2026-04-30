package api.gymmanagment.Repositories;

import api.gymmanagment.Entities.Gym;
import api.gymmanagment.Entities.Member;
import api.gymmanagment.Enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByPlanId(Long planId);
    List<Member> findByStatus(MemberStatus status);
    long countAllByPlanIdAndStatus(Long planId, MemberStatus status);
    List<Member> findAllByPlanId(Long planId);

}
