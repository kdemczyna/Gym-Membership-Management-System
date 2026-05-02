package api.gymmanagement.services;

import api.gymmanagement.DTOs.PlanRequest;
import api.gymmanagement.DTOs.PlanResponse;
import api.gymmanagement.entities.Gym;
import api.gymmanagement.entities.MembershipPlan;
import api.gymmanagement.entities.MembershipPrice;
import api.gymmanagement.repositories.MembershipPlanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MembershipPlanService {

    private final MembershipPlanRepository planRepository;
    private final GymService gymService;

    public MembershipPlanService(MembershipPlanRepository planRepository, GymService gymService) {
        this.planRepository = planRepository;
        this.gymService = gymService;
    }

    public PlanResponse createPlan(Long gymId, PlanRequest request) {
        Gym gym = gymService.getGymEntityById(gymId);

        MembershipPlan plan = new MembershipPlan();
        plan.setGym(gym);
        plan.setName(request.name());
        plan.setType(request.type());
        plan.setDurationMonths(request.durationMonths());
        plan.setMaxMembers(request.maxMembers());

        // set the initial price
        MembershipPrice price = new MembershipPrice();
        price.setPlan(plan);
        price.setCurrency(request.currency().toUpperCase());
        price.setMonthlyPrice(request.monthlyPrice());
        plan.getPrices().add(price);

        MembershipPlan saved = planRepository.save(plan);
        return toResponse(saved);
    }

    public List<PlanResponse> getPlansForGym(Long gymId) {
        gymService.getGymEntityById(gymId); // validates gym exists
        return planRepository.findByGymId(gymId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MembershipPlan getPlanEntityById(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership plan not found"));
    }

    private PlanResponse toResponse(MembershipPlan plan) {
        Map<String, java.math.BigDecimal> priceMap = plan.getPrices().stream()
                .collect(Collectors.toMap(
                        MembershipPrice::getCurrency,
                        MembershipPrice::getMonthlyPrice
                ));
        return new PlanResponse(
                plan.getId(),
                plan.getGym().getId(),
                plan.getGym().getName(),
                plan.getName(),
                plan.getType(),
                plan.getDurationMonths(),
                plan.getMaxMembers(),
                priceMap
        );
    }
}