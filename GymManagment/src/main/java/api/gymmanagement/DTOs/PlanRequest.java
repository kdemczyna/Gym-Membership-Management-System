package api.gymmanagement.DTOs;

import api.gymmanagement.enums.PlanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PlanRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Type is required")
        PlanType type,

        @NotNull(message = "Duration is required")
        @Min(value = 1, message = "Duration must be at least 1 month")
        Integer durationMonths,

        @NotNull(message = "Max members is required")
        @Min(value = 1, message = "Max members must be at least 1")
        Integer maxMembers,

        @NotNull(message = "Monthly price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal monthlyPrice,

        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code e.g. EUR")
        String currency
) {}