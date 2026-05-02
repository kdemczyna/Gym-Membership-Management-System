package api.gymmanagement.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GymRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Phone number must be between 9 and 15 digits, optionally starting with +")
        String phone
) {}