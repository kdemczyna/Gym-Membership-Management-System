package api.gymmanagement.DTOs;

import api.gymmanagement.enums.PlanType;

import java.math.BigDecimal;
import java.util.Map;

public record PlanResponse(
        Long id,
        Long gymId,
        String gymName,
        String name,
        PlanType type,
        Integer durationMonths,
        Integer maxMembers,
        Map<String, BigDecimal> prices
) {}