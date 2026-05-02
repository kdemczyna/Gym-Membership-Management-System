package api.gymmanagement.controllers;

import api.gymmanagement.DTOs.GymRequest;
import api.gymmanagement.DTOs.GymResponse;
import api.gymmanagement.services.GymService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

    private final GymService gymService;

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    // POST /api/gyms
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GymResponse createGym(@Valid @RequestBody GymRequest request) {
        return gymService.createGym(request);
    }

    // GET /api/gyms
    @GetMapping
    public List<GymResponse> getAllGyms() {
        return gymService.getAllGyms();
    }
}