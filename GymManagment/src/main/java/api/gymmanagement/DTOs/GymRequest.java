package api.gymmanagement.DTOs;

import jakarta.validation.constraints.NotBlank;

public record GymRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Phone is required")
        String phone
) {}