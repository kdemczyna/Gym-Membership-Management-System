package api.gymmanagement.DTOs;

import api.gymmanagement.enums.MemberStatus;

import java.time.LocalDate;

public record MemberResponse(
        Long id,
        String fullName,
        String email,
        String address,
        LocalDate membershipStartDate,
        MemberStatus status,
        String planName,
        String gymName
) {}