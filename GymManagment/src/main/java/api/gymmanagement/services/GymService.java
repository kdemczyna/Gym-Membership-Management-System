package api.gymmanagement.services;

import api.gymmanagement.DTOs.GymRequest;
import api.gymmanagement.DTOs.GymResponse;
import api.gymmanagement.entities.Gym;
import api.gymmanagement.repositories.GymRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GymService {

    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    public GymResponse createGym(GymRequest request) {
        if (gymRepository.existsByName(request.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Gym with this name already exists");
        }
        if (gymRepository.existsByPhone(request.phone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }
        Gym gym = new Gym();
        gym.setName(request.name());
        gym.setAddress(request.address());
        gym.setPhone(request.phone());
        Gym saved = gymRepository.save(gym);
        return toResponse(saved);
    }

    public List<GymResponse> getAllGyms() {
        return gymRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Gym getGymEntityById(Long id) {
        return gymRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gym not found"));
    }

    private GymResponse toResponse(Gym gym) {
        return new GymResponse(gym.getId(), gym.getName(), gym.getAddress(), gym.getPhone());
    }
}