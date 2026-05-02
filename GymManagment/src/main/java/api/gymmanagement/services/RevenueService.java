package api.gymmanagement.services;

import api.gymmanagement.DTOs.RevenueReportResponse;
import api.gymmanagement.entities.Member;
import api.gymmanagement.entities.MembershipPlan;
import api.gymmanagement.entities.MembershipPrice;
import api.gymmanagement.enums.MemberStatus;
import api.gymmanagement.repositories.GymRepository;
import api.gymmanagement.entities.Gym;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RevenueService {

    private final GymRepository gymRepository;

    public RevenueService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    public List<RevenueReportResponse> getRevenueReport() {
        List<Gym> gyms = gymRepository.findAll();
        List<RevenueReportResponse> report = new ArrayList<>();

        for (Gym gym : gyms) {
            // map of currency -> total revenue for this gym
            Map<String, BigDecimal> revenuePerCurrency = new HashMap<>();

            for (MembershipPlan plan : gym.getMembershipPlans()) {
                long activeCount = plan.getMembers().stream()
                        .filter(m -> m.getStatus() == MemberStatus.ACTIVE)
                        .count();

                if (activeCount == 0) continue;

                for (MembershipPrice price : plan.getPrices()) {
                    BigDecimal planRevenue = price.getMonthlyPrice()
                            .multiply(BigDecimal.valueOf(activeCount));

                    revenuePerCurrency.merge(price.getCurrency(), planRevenue, BigDecimal::add);
                }
            }

            // one entry per currency per gym
            for (Map.Entry<String, BigDecimal> entry : revenuePerCurrency.entrySet()) {
                report.add(new RevenueReportResponse(gym.getName(), entry.getValue(), entry.getKey()));
            }
        }

        return report;
    }
}