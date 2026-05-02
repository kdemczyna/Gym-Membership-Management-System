package api.gymmanagement.controllers;

import api.gymmanagement.DTOs.PlanRequest;
import api.gymmanagement.DTOs.PlanResponse;
import api.gymmanagement.services.MembershipPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gyms/{gymId}/plans")
public class MembershipPlanController {

    private final MembershipPlanService planService;

    public MembershipPlanController(MembershipPlanService planService) {
        this.planService = planService;
    }

    // POST /api/gyms/{gymId}/plans
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanResponse createPlan(@PathVariable Long gymId,
                                   @Valid @RequestBody PlanRequest request) {
        return planService.createPlan(gymId, request);
    }

    // GET /api/gyms/{gymId}/plans
    @GetMapping
    public List<PlanResponse> getPlansForGym(@PathVariable Long gymId) {
        return planService.getPlansForGym(gymId);
    }
}