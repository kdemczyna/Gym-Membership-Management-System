package api.gymmanagement.DTOs;

public record GymResponse(
        Long id,
        String name,
        String address,
        String phone
) {}