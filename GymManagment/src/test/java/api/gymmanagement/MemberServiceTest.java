package api.gymmanagement;

import api.gymmanagement.DTOs.MemberRequest;
import api.gymmanagement.DTOs.MemberResponse;
import api.gymmanagement.entities.Gym;
import api.gymmanagement.entities.Member;
import api.gymmanagement.entities.MembershipPlan;
import api.gymmanagement.enums.MemberStatus;
import api.gymmanagement.enums.PlanType;
import api.gymmanagement.repositories.MemberRepository;
import api.gymmanagement.services.MemberService;
import api.gymmanagement.services.MembershipPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipPlanService planService;

    @InjectMocks
    private MemberService memberService;

    private MembershipPlan plan;
    private MemberRequest memberRequest;
    private Gym gym;

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
        plan.setMaxMembers(2);
        plan.setMembers(new ArrayList<>());

        memberRequest = new MemberRequest("Jan", "Kowalski", "jan@email.com", "ul. Testowa 1");
    }

    //registerMember
    @Test
    void registerMember_success() {
        when(planService.getPlanEntityById(1L)).thenReturn(plan);
        when(memberRepository.existsByEmail("jan@email.com")).thenReturn(false);

        Member savedMember = new Member(plan, "Jan", "Kowalski", "jan@email.com", "ul. Testowa 1");
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        MemberResponse response = memberService.registerMember(1L, memberRequest);

        assertThat(response.fullName()).isEqualTo("Jan Kowalski");
        assertThat(response.status()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(response.planName()).isEqualTo("Basic Monthly");
        assertThat(response.gymName()).isEqualTo("FitLife Center");
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void registerMember_planFull_throwsConflict() {
        // fill plan to max capacity
        Member m1 = new Member(plan, "A", "B", "a@email.com", null);
        Member m2 = new Member(plan, "C", "D", "c@email.com", null);
        plan.getMembers().add(m1);
        plan.getMembers().add(m2);

        when(planService.getPlanEntityById(1L)).thenReturn(plan);

        assertThatThrownBy(() -> memberService.registerMember(1L, memberRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Membership plan is full");

        verify(memberRepository, never()).save(any());
    }

    @Test
    void registerMember_duplicateEmail_throwsConflict() {
        when(planService.getPlanEntityById(1L)).thenReturn(plan);
        when(memberRepository.existsByEmail("jan@email.com")).thenReturn(true);

        assertThatThrownBy(() -> memberService.registerMember(1L, memberRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email already registered");

        verify(memberRepository, never()).save(any());
    }

    @Test
    void registerMember_cancelledMemberDoesNotCountTowardCapacity() {
        // add 1 active and 1 cancelled (plan with maxMembers=2 should still accept new member)
        Member active = new Member(plan, "A", "B", "a@email.com", null);
        Member cancelled = new Member(plan, "C", "D", "c@email.com", null);
        cancelled.cancel();
        plan.getMembers().add(active);
        plan.getMembers().add(cancelled);

        when(planService.getPlanEntityById(1L)).thenReturn(plan);
        when(memberRepository.existsByEmail("jan@email.com")).thenReturn(false);

        Member savedMember = new Member(plan, "Jan", "Kowalski", "jan@email.com", null);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        MemberResponse response = memberService.registerMember(1L, memberRequest);

        assertThat(response.status()).isEqualTo(MemberStatus.ACTIVE);
    }

    //cancelMembership
    @Test
    void cancelMembership_success() {
        Member member = new Member(plan, "Jan", "Kowalski", "jan@email.com", null);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponse response = memberService.cancelMembership(1L);

        assertThat(response.status()).isEqualTo(MemberStatus.CANCELLED);
    }

    @Test
    void cancelMembership_alreadyCancelled_throwsConflict() {
        Member member = new Member(plan, "Jan", "Kowalski", "jan@email.com", null);
        member.cancel();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.cancelMembership(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("already cancelled");
    }

    @Test
    void cancelMembership_notFound_throwsNotFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.cancelMembership(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Member not found");
    }

    //reactivateMembership
    @Test
    void reactivateMembership_success() {
        Member member = new Member(plan, "Jan", "Kowalski", "jan@email.com", null);
        member.cancel();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponse response = memberService.reactivateMembership(1L);

        assertThat(response.status()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void reactivateMembership_planFull_throwsConflict() {
        Member m1 = new Member(plan, "A", "B", "a@email.com", null);
        Member m2 = new Member(plan, "C", "D", "c@email.com", null);
        plan.getMembers().add(m1);
        plan.getMembers().add(m2);

        Member member = new Member(plan, "Jan", "Kowalski", "jan@email.com", null);
        member.cancel();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.reactivateMembership(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("plan is full");
    }

    @Test
    void reactivateMembership_alreadyActive_throwsConflict() {
        Member member = new Member(plan, "Jan", "Kowalski", "jan@email.com", null);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.reactivateMembership(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("already active");
    }
}