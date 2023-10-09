package be.ucll.ipminor341t.springrest.boat;

import be.ucll.ipminor341t.domain.Boat;
import be.ucll.ipminor341t.domain.BoatRepository;
import be.ucll.ipminor341t.domain.service.BoatService;
import be.ucll.ipminor341t.generic.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoatServiceTest {

    @Mock
    BoatRepository boatRepository;

    @InjectMocks
    BoatService boatService;

    private Boat boat1, boat2;

    @BeforeEach
    public void setUp() {
        boat1 = BoatBuilder.aTeamA().build();
        boat1.setId(1L);
        boat2 = BoatBuilder.aTeamB().build();
        boat2.setId(2L);
    }

    //Happy Case
    @Test
    public void givenBoats_whenUpdatingBoat_ThenBoatThatIsUpdatedIsReturned() {

        when(boatRepository.findById(boat1.getId())).thenReturn(Optional.ofNullable(boat1));
        when(boatRepository.save(any())).thenReturn(boat2);

        Boat updatedBoat = boatService.updateBoat(boat1.getId(), boat2);

        assertEquals(updatedBoat.getInsurance(), boat2.getInsurance());
        assertEquals(updatedBoat.getName(), boat2.getName());
        assertEquals(updatedBoat.getHeight(), boat2.getHeight());
        assertEquals(updatedBoat.getWidth(), boat2.getWidth());
        assertEquals(updatedBoat.getEmail(), boat2.getEmail());
    }

    //Unhappy Case
    @Test
    public void whenUpdatingWithInvalidID_ThenBoatIsNotUpdatedAndErrorIsReturned() {;

        final Throwable raisedException = catchThrowable(() -> boatService.updateBoat(10L, boat1));

        assertThat(raisedException).isInstanceOf(ServiceException.class);

        ServiceException serviceException = (ServiceException) raisedException;

        assertEquals("updateError", serviceException.getAction());
    }

    //Unhappy Case
    @Test
    public void whenSearchingWithInvalidInsuranceNumber_ThenErrorIsReturned() {;

        final Throwable raisedException = catchThrowable(() -> boatService.getBoatsByInsurance(""));

        assertThat(raisedException).isInstanceOf(ServiceException.class);

        ServiceException serviceException = (ServiceException) raisedException;

        assertEquals("invalidInsurance", serviceException.getAction());
    }

    //Unhappy Case
    @Test
    public void whenSearchingWithNegativeNumbers_ThenErrorIsReturned() {;

        final Throwable raisedException = catchThrowable(() -> boatService.getBoatsByHeightAndWidth(-20,-100));

        assertThat(raisedException).isInstanceOf(ServiceException.class);

        ServiceException serviceException = (ServiceException) raisedException;

        assertEquals("invalidHeightOrWidth", serviceException.getAction());
    }

    //Happy Case
    @Test
    public void givenBoats_whenDeletingBoat_ThenBoatIsDeletedIsReturned() {

        List<Boat> boats = new ArrayList<>(Arrays.asList(boat1, boat2));

        when(boatRepository.findById(boat1.getId())).thenReturn(Optional.ofNullable(boat1));
        when(boatService.deleteBoat(boat1.getId())).thenAnswer(invocationOnMock -> {
            boats.removeIf(b -> b.getId().equals(boat1.getId()));
            return boat1;
        });
        when(boatService.getBoats()).thenReturn(boats);

        Boat deletedBoat = boatService.deleteBoat(boat1.getId());

        assertThat(deletedBoat).isEqualTo(boat1);
        assertThat(boatService.getBoats()).doesNotContain(boat1);

    }

    //Unhappy Case
    @Test
    public void whenDeletingWithInvalidID_ThenBoatIsNotDeletedAndErrorIsReturned() {;

        final Throwable raisedException = catchThrowable(() -> boatService.deleteBoat(10L));

        assertThat(raisedException).isInstanceOf(ServiceException.class);

        ServiceException serviceException = (ServiceException) raisedException;

        assertEquals("deleteError", serviceException.getAction());
    }

}
