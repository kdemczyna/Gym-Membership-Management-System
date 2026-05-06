package api.gymmanagement;

import api.gymmanagement.DTOs.GymRequest;
import api.gymmanagement.DTOs.GymResponse;
import api.gymmanagement.entities.Gym;
import api.gymmanagement.repositories.GymRepository;
import api.gymmanagement.services.GymService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymServiceTest {

    @Mock
    private GymRepository gymRepository;

    @InjectMocks
    private GymService gymService;

    private Gym gym;
    private GymRequest gymRequest;

    @BeforeEach
    void setUp() {
        gymRequest = new GymRequest("FitLife Center", "ul. Sportowa 1, Warszawa", "+48100200300");

        gym = new Gym();
        gym.setName("FitLife Center");
        gym.setAddress("ul. Sportowa 1, Warszawa");
        gym.setPhone("+48100200300");
    }

    //createGym

    @Test
    void createGym_success() {
        when(gymRepository.existsByName("FitLife Center")).thenReturn(false);
        when(gymRepository.existsByPhone("+48100200300")).thenReturn(false);
        when(gymRepository.save(any(Gym.class))).thenReturn(gym);

        GymResponse response = gymService.createGym(gymRequest);

        assertThat(response.name()).isEqualTo("FitLife Center");
        assertThat(response.address()).isEqualTo("ul. Sportowa 1, Warszawa");
        assertThat(response.phone()).isEqualTo("+48100200300");
        verify(gymRepository).save(any(Gym.class));
    }

    @Test
    void createGym_duplicateName_throwsConflict() {
        when(gymRepository.existsByName("FitLife Center")).thenReturn(true);

        assertThatThrownBy(() -> gymService.createGym(gymRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Gym with this name already exists");

        verify(gymRepository, never()).save(any());
    }

    @Test
    void createGym_duplicatePhone_throwsConflict() {
        when(gymRepository.existsByName("FitLife Center")).thenReturn(false);
        when(gymRepository.existsByPhone("+48100200300")).thenReturn(true);

        assertThatThrownBy(() -> gymService.createGym(gymRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Phone number already exists");

        verify(gymRepository, never()).save(any());
    }

    // getAllGyms
    @Test
    void getAllGyms_returnsAllGyms() {
        Gym gym2 = new Gym();
        gym2.setName("Iron Gym");
        gym2.setAddress("ul. Stalowa 9, Kraków");
        gym2.setPhone("+48100200301");

        when(gymRepository.findAll()).thenReturn(List.of(gym, gym2));

        List<GymResponse> result = gymService.getAllGyms();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("FitLife Center");
        assertThat(result.get(1).name()).isEqualTo("Iron Gym");
    }

    @Test
    void getAllGyms_empty_returnsEmptyList() {
        when(gymRepository.findAll()).thenReturn(List.of());

        List<GymResponse> result = gymService.getAllGyms();

        assertThat(result).isEmpty();
    }

    // getGymEntityById
    @Test
    void getGymEntityById_exists_returnsGym() {
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));

        Gym result = gymService.getGymEntityById(1L);

        assertThat(result.getName()).isEqualTo("FitLife Center");
    }

    @Test
    void getGymEntityById_notFound_throwsNotFound() {
        when(gymRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gymService.getGymEntityById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Gym not found");
    }
}