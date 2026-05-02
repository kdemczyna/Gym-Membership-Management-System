package api.gymmanagement.services;

import api.gymmanagement.DTOs.MemberRequest;
import api.gymmanagment.DTOs.MemberResponse;
import api.gymmanagement.entities.Member;
import api.gymmanagement.entities.MembershipPlan;
import api.gymmanagement.enums.MemberStatus;
import api.gymmanagement.repositories.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MembershipPlanService planService;

    public MemberService(MemberRepository memberRepository, MembershipPlanService planService) {
        this.memberRepository = memberRepository;
        this.planService = planService;
    }

    public MemberResponse registerMember(Long planId, MemberRequest request) {
        MembershipPlan plan = planService.getPlanEntityById(planId);

        // plan capacity check
        if (plan.isFull()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Membership plan is full");
        }

        //duplicated email check
        if (memberRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        Member member = new Member(plan, request.name(), request.surname(), request.email(), request.address());
        Member saved = memberRepository.save(member);
        return toResponse(saved);
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MemberResponse cancelMembership(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        if (member.getStatus() == MemberStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Membership is already cancelled");
        }

        member.cancel();
        Member saved = memberRepository.save(member);
        return toResponse(saved);
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getFullName(),
                member.getEmail(),
                member.getAddress(),
                member.getMembershipStartDate(),
                member.getStatus(),
                member.getPlan().getName(),
                member.getPlan().getGym().getName()
        );
    }

    // membership reactivation
    public MemberResponse reactivateMembership(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        if (member.getStatus() == MemberStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Membership is already active");
        }

        // capacity check
        if (member.getPlan().isFull()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Membership plan is full");
        }

        member.setStatus(MemberStatus.ACTIVE);
        return toResponse(memberRepository.save(member));
    }
}