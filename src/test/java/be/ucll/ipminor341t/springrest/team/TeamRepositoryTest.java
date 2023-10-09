package be.ucll.ipminor341t.springrest.team;

import be.ucll.ipminor341t.domain.Team;
import be.ucll.ipminor341t.domain.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeamRepository teamRepository;

    //Happy Case
    @Test
    public void givenTeamRegistered_whenFindByNameAndCategory_thenReturnTeam() {
        // given
        Team team = TeamBuilder.aTeam().build();
        entityManager.persistAndFlush(team);

        // when
        Team found = teamRepository.findByNameAndCategory(team.getName(), team.getCategory());

        // then
        assertNotNull(found);
        assertThat(found.getName()).isEqualTo(team.getName());

    }

    //Happy Case
    @Test
    public void givenTeamRegistered_whenFindByNumberOfCrewLessThanOrderByNumberOfCrewDesc_thenReturnTeamWithLessCrew() {
        // given
        Team team = TeamBuilder.aTeam().withName("Team1").withNumberOfCrew(1).build();
        entityManager.persistAndFlush(team);
        Team team2 = TeamBuilder.aTeam().withName("Team2").withNumberOfCrew(2).build();
        entityManager.persistAndFlush(team2);
        Team team3 = TeamBuilder.aTeam().withName("Team3").withNumberOfCrew(3).build();
        entityManager.persistAndFlush(team3);

        // when
        List<Team> found = teamRepository.findByPassengersLessThanOrderByPassengersDesc(3);

        // then
        assertNotNull(found);
        assertThat(found.size()).isEqualTo(2);
        assertThat(found.get(0).getName()).isEqualTo(team2.getName());
    }

    //Happy Case
    @Test
    public void givenTeamRegistered_whenFindByCategoryIgnoreCase_thenReturnTeamWithCategory() {
        // given
        Team team = TeamBuilder.aTeam().withName("Team1").withCategory("AZERTYU").build();
        entityManager.persistAndFlush(team);

        // when
        List<Team> found = teamRepository.findByCategoryIgnoreCase("AZerTYU");

        // then
        assertNotNull(found);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getName()).isEqualTo(team.getName());
    }
}






