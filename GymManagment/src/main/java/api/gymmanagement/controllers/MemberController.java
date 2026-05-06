package api.gymmanagement.controllers;

import api.gymmanagement.DTOs.MemberRequest;
import api.gymmanagement.DTOs.MemberResponse;
import api.gymmanagement.services.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // POST /api/plans/{planId}/members
    @PostMapping("/plans/{planId}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse registerMember(@PathVariable Long planId,
                                         @Valid @RequestBody MemberRequest request) {
        return memberService.registerMember(planId, request);
    }

    // GET /api/members
    @GetMapping("/members")
    public List<MemberResponse> getAllMembers() {
        return memberService.getAllMembers();
    }

    // PATCH /api/members/{memberId}/cancel
    @PatchMapping("/members/{memberId}/cancel")
    public MemberResponse cancelMembership(@PathVariable Long memberId) {
        return memberService.cancelMembership(memberId);
    }

    @PatchMapping("/members/{memberId}/reactivate")
    public MemberResponse reactivateMembership(@PathVariable Long memberId) {
        return memberService.reactivateMembership(memberId);
    }
}