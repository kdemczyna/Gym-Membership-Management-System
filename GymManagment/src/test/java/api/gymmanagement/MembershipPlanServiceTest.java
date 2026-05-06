package api.gymmanagement;

import api.gymmanagement.DTOs.PlanRequest;
import api.gymmanagement.DTOs.PlanResponse;
import api.gymmanagement.entities.Gym;
import api.gymmanagement.entities.MembershipPlan;
import api.gymmanagement.enums.PlanType;
import api.gymmanagement.repositories.MembershipPlanRepository;
import api.gymmanagement.services.GymService;
import api.gymmanagement.services.MembershipPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipPlanServiceTest {

    @Mock
    private MembershipPlanRepository planRepository;

    @Mock
    private GymService gymService;

    @InjectMocks
    private MembershipPlanService planService;

    private Gym gym;
    private MembershipPlan plan;
    private PlanRequest planRequest;

    @BeforeEach
    void setUp() {
        gym = new Gym();
        gym.setName("FitLife Center");
        gym.setAddress("ul. Sportowa 1, Warszawa");
        gym.setPhone("+48100200300");

        plan = new MembershipPlan();
        plan.setGym(gym);
        plan.setName("Basic Monthly");
        plan.setType(PlanType.BASIC);
        plan.setDurationMonths(1);
        plan.setMaxMembers(10);
        plan.setPrices(new ArrayList<>());
        plan.setMembers(new ArrayList<>());

        planRequest = new PlanRequest(
                "Basic Monthly",
                PlanType.BASIC,
                1,
                10,
                new BigDecimal("49.99"),
                "EUR"
        );
    }

    //createPlan
    @Test
    void createPlan_success() {
        when(gymService.getGymEntityById(1L)).thenReturn(gym);
        when(planRepository.save(any(MembershipPlan.class))).thenReturn(plan);

        PlanResponse response = planService.createPlan(1L, planRequest);

        assertThat(response.name()).isEqualTo("Basic Monthly");
        assertThat(response.type()).isEqualTo(PlanType.BASIC);
        assertThat(response.maxMembers()).isEqualTo(10);
        assertThat(response.gymName()).isEqualTo("FitLife Center");
        verify(planRepository).save(any(MembershipPlan.class));
    }

    @Test
    void createPlan_gymNotFound_throwsNotFound() {
        when(gymService.getGymEntityById(99L))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Gym not found"));

        assertThatThrownBy(() -> planService.createPlan(99L, planRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Gym not found");

        verify(planRepository, never()).save(any());
    }

    //getPlansForGym
    @Test
    void getPlansForGym_returnsPlans() {
        when(gymService.getGymEntityById(1L)).thenReturn(gym);
        when(planRepository.findByGymId(1L)).thenReturn(List.of(plan));

        List<PlanResponse> result = planService.getPlansForGym(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Basic Monthly");
    }

    @Test
    void getPlansForGym_noPlans_returnsEmptyList() {
        when(gymService.getGymEntityById(1L)).thenReturn(gym);
        when(planRepository.findByGymId(1L)).thenReturn(List.of());

        List<PlanResponse> result = planService.getPlansForGym(1L);

        assertThat(result).isEmpty();
    }

    //getPlanEntityById
    @Test
    void getPlanEntityById_exists_returnsPlan() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        MembershipPlan result = planService.getPlanEntityById(1L);

        assertThat(result.getName()).isEqualTo("Basic Monthly");
    }

    @Test
    void getPlanEntityById_notFound_throwsNotFound() {
        when(planRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> planService.getPlanEntityById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Membership plan not found");
    }
}