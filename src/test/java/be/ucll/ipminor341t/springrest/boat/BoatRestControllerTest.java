package be.ucll.ipminor341t.springrest.boat;

import be.ucll.ipminor341t.IpMinor341tApplication;
import be.ucll.ipminor341t.domain.Boat;
import be.ucll.ipminor341t.domain.service.BoatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = IpMinor341tApplication.class)
@AutoConfigureMockMvc
public class BoatRestControllerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private BoatService boatService;

    @Autowired
    MockMvc boatRestController;

    private Boat boat1, boat2, boat3;

    @BeforeEach
    public void setUp() {
        boat1 = BoatBuilder.aTeamA().build();

        boat2 = BoatBuilder.aTeamB().build();

        boat3 = BoatBuilder.anInvalidTeamWithNoName().build();
    }

    //Happy case
    @Test
    public void givenAllBoat_ReturnsBoatsInJson() throws Exception {
        List<Boat> boats = Arrays.asList(boat1,boat2);

        given(boatService.getBoats()).willReturn(boats);

        boatRestController.perform(get("/api/boat/overview").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(boat1.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(boat2.getName())));
    }

    //Happy case
    @Test
    public void addValidBoat_ReturnsBoatInJson() throws Exception {

        when(boatService.createBoat(any(Boat.class))).thenReturn(boat2);

        boatRestController.perform(post("/api/boat/add").content(mapper.writeValueAsString(boat2)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is(boat2.getName())));
    }

    //Unhappy case
    @Test
    public void addNotValidBoat_ReturnsErrorInJson() throws Exception {
        boatRestController.perform(post("/api/boat/add").content(mapper.writeValueAsString(boat3)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("name.missing")));
    }
}
