package be.ucll.ipminor341t.domain.service;

import be.ucll.ipminor341t.domain.Team;
import be.ucll.ipminor341t.domain.TeamRepository;
import be.ucll.ipminor341t.generic.ServiceException;
import be.ucll.ipminor341t.web.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team findById(long id) {
        return teamRepository.findById(id).orElseThrow(() -> new ServiceException("error", "Team met opgegeven id bestaat niet"));
    }

    public Team createTeam(TeamDto dto) {
        if (teamRepository.findByNameAndCategory(dto.getName(), dto.getCategory()) != null)
            throw new ServiceException("add", "team.already.exists");

        Team team = new Team();
        team.setId(dto.getId());
        team.setName(dto.getName());
        team.setCategory(dto.getCategory());
        team.setPassengers(dto.getPassengers());
        team.setClub(dto.getClub());

        return teamRepository.save(team);
    }

    public Team deleteTeam(long id) {
        Team team = findById(id);

        if (team.getRegattas().size() > 0) {
            throw new ServiceException("update", "team.has.regattas");
        }
        teamRepository.deleteById(id);
        return team;
    }

    public Team updateTeam(Long id, TeamDto updatedTeam) {
        Team team = findById(id);

        if (team.getRegattas().size() > 0) {
            throw new ServiceException("update", "team.has.regattas");
        }
        team.setName(updatedTeam.getName());
        team.setCategory(updatedTeam.getCategory());
        team.setPassengers(updatedTeam.getPassengers());
        team.setClub(updatedTeam.getClub());

        return teamRepository.save(team);
    }

    public List<Team> findByCategoryIgnoreCase(String category) {
        return teamRepository.findByCategoryIgnoreCase(category);
    }

    public List<Team> findByNumberOfCrewLessThanOrderByNumberOfCrewDesc(int lessThan) {
        return teamRepository.findByPassengersLessThanOrderByPassengersDesc(lessThan);
    }


}
