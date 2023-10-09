package be.ucll.ipminor341t.springrest.boat;

import be.ucll.ipminor341t.domain.Boat;
import be.ucll.ipminor341t.domain.BoatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BoatRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoatRepository boatRepository;

    private Boat boat1;
    private Boat boat2;

    @BeforeEach
    public void setUp() {
        boat1 = BoatBuilder.aTeamA().build();
        boat2 = BoatBuilder.aTeamB().build();
    }

    //Happy Case
    @Test
    public void givenBoat_whenFindByInsurance_thenBoatsWithInsuranceAreReturned() {
        entityManager.merge(boat1);
        entityManager.merge(boat2);

        List<Boat> boats = boatRepository.findAllByInsurance(boat1.getInsurance());

        assertEquals(1, boats.size());
        assertEquals(boat1.getInsurance(), boats.get(0).getInsurance());
    }

    //Unhappy Case
    @Test
    public void givenNoBoats_whenFindByInsurance_thenEmptyListIsReturned() {
        List<Boat> boats = boatRepository.findAllByInsurance("1A784B");
        assertTrue(boats.isEmpty());
    }

    //Happy Case
    @Test
    public void givenBoats_whenFindByHeightAndWidth_thenBoatsWithHeightAndWidthAreReturned() {
        entityManager.merge(boat1);
        entityManager.merge(boat2);

        List<Boat> boats = boatRepository.findAllByHeightAndWidth(boat1.getHeight(), boat1.getWidth());

        assertEquals(1, boats.size());
        assertEquals(boat1.getHeight(), boats.get(0).getHeight());
        assertEquals(boat1.getWidth(), boats.get(0).getWidth());
    }

    //Unhappy Case
    @Test
    public void givenNoBoats_whenFindByHeightAndWidth_thenEmptyListIsReturned() {
        List<Boat> boats = boatRepository.findAllByHeightAndWidth(1.7, 2.5);
        assertTrue(boats.isEmpty());
    }
}