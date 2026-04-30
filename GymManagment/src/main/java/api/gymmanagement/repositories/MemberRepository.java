package api.gymmanagement.repositories;

import api.gymmanagement.entities.Member;
import api.gymmanagement.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByPlanId(Long planId);
    List<Member> findByStatus(MemberStatus status);
    long countAllByPlanIdAndStatus(Long planId, MemberStatus status);
    List<Member> findAllByPlanId(Long planId);

}
