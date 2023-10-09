package be.ucll.ipminor341t.springrest.team;

import be.ucll.ipminor341t.domain.Team;
import be.ucll.ipminor341t.domain.TeamRepository;
import be.ucll.ipminor341t.domain.service.TeamService;
import be.ucll.ipminor341t.generic.ServiceException;
import be.ucll.ipminor341t.web.TeamDto;
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
public class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamService teamService;

    //Happy Case
    @Test
    public void givenNoTeams_whenValidTeamAdded_ThenTeamIsAddedAndTeamIsReturned() {
        // given
        Team team = TeamBuilder.aTeamA().build();
        TeamDto teamDto = TeamDtoBuilder.aTeamA().build();

        when(teamRepository.findByNameAndCategory(team.getName(), team.getCategory())).thenReturn(null);
        when(teamRepository.save(any())).thenReturn(team);

        // when
        Team addedTeam = teamService.createTeam(teamDto);

        // then
        assertThat(team.getName()).isSameAs(addedTeam.getName());
    }

    //Unhappy Case
    @Test
    public void givenTeams_whenValidTeamAddedWithAlreadyUsedNameAndCategory_ThenTeamIsNotAddedAndErrorIsReturned() {
        // given
        Team team = TeamBuilder.aTeamA().build();
        TeamDto teamDto = TeamDtoBuilder.aTeamA().build();

        when(teamRepository.findByNameAndCategory(team.getName(), team.getCategory())).thenReturn(team);

        // when
        final Throwable raisedException = catchThrowable(() -> teamService.createTeam(teamDto));

        // then
        assertThat(raisedException).isInstanceOf(ServiceException.class)
                .hasMessageContaining("team.already.exists");
    }

    //Happy Case
    @Test
    public void givenTeams_whenDeleteTeamWithId_ThenTeamIsDeleted() {
        Team team = TeamBuilder.aTeamA().build();
        team.setId(1L);
        Team team2 = TeamBuilder.aTeamB().build();
        team2.setId(2L);

        List<Team> teams = new ArrayList<>(Arrays.asList(team, team2));

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamService.deleteTeam(team.getId())).thenAnswer(invocationOnMock -> {
            teams.removeIf(b -> b.getId().equals(team.getId()));
            return null;
        });
        when(teamService.getAllTeams()).thenReturn(teams);

        Team deletedTeam = teamService.deleteTeam(team.getId());

        assertThat(deletedTeam).isEqualTo(team);
        assertThat(teamService.getAllTeams()).doesNotContain(team);
        assertEquals(1, teams.size());
    }

    //Unhappy Case
    @Test
    public void whenDeleteTeamWithId_ThenTeamIsNotDeletedAndErrorIsReturned() {

        final Throwable raisedException = catchThrowable(() -> teamService.deleteTeam(15L));

        assertThat(raisedException).isInstanceOf(ServiceException.class)
                .hasMessageContaining("Team met opgegeven id bestaat niet");

        ServiceException serviceException = (ServiceException) raisedException;

        assertEquals("error", serviceException.getAction());
    }


}