package be.ucll.ipminor341t.springrest.team;

import be.ucll.ipminor341t.IpMinor341tApplication;
import be.ucll.ipminor341t.domain.Team;
import be.ucll.ipminor341t.domain.service.TeamService;
import be.ucll.ipminor341t.web.TeamDto;
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
import java.util.Collections;
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
public class TeamRestControllerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private TeamService teamService;
    @Autowired
    MockMvc teamRestController;

    private Team teamA, teamB;
    private TeamDto teamADto, teamBDto, teamNoNameDto;

    @BeforeEach
    public void setUp() {
        teamA = TeamBuilder.aTeamA().build();
        teamB = TeamBuilder.aTeamB().build();

        teamADto = TeamDtoBuilder.aTeamA().build();
        teamBDto = TeamDtoBuilder.aTeamB().build();
        teamNoNameDto = TeamDtoBuilder.anInvalidTeamWithNoName().build();
    }

    //Happy Case
    @Test
    public void givenAllTeam_ReturnsTeamsInJson() throws Exception {
        List<Team> teams = Arrays.asList(teamA, teamB);

        given(teamService.getAllTeams()).willReturn(teams);

        teamRestController.perform(get("/api/team/overview").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(teamADto.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(teamBDto.getName())));
    }

    //Happy Case
    @Test
    public void addValidTeam_ReturnsTeamInJson() throws Exception {
        when(teamService.createTeam(any(TeamDto.class))).thenReturn(teamB);
        when(teamService.getAllTeams()).thenReturn(Collections.singletonList(teamB));

        teamRestController.perform(post("/api/team/add").content(mapper.writeValueAsString(teamBDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is(teamBDto.getName())));

    }

    //Unhappy Case
    @Test
    public void addNotValidTeam_ReturnsErrorInJson() throws Exception {
        teamRestController.perform(post("/api/team/add").content(mapper.writeValueAsString(teamNoNameDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("name.missing")));
    }
}
