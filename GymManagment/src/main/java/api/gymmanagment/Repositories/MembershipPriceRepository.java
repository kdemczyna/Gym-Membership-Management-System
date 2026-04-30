package api.gymmanagment.Repositories;

import api.gymmanagment.Entities.MembershipPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipPriceRepository extends JpaRepository<MembershipPrice, Long> {
    List<MembershipPrice> findByPlanId(Long planId);
    Optional<MembershipPrice> findByPlanIdAndCurrency(Long planId, String currency);
}
