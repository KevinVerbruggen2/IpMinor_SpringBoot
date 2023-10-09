package be.ucll.ipminor341t.domain.service;

import be.ucll.ipminor341t.domain.Regatta;
import be.ucll.ipminor341t.domain.RegattaRepository;
import be.ucll.ipminor341t.domain.Team;
import be.ucll.ipminor341t.domain.TeamRepository;
import be.ucll.ipminor341t.generic.ServiceException;
import be.ucll.ipminor341t.web.RegattaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class RegattaService {

    @Autowired
    private RegattaRepository regattaRepository;
    @Autowired
    private TeamRepository teamRepository;

    public Page<Regatta> getRegattas(String size, String page, Sort sort) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), sort);
        return regattaRepository.findAll(pageable);
    }

    public Page<Regatta> getRegattaByCategoryContainsIgnoreCase(String category, String size) {
        Pageable page = PageRequest.of(0, Integer.parseInt(size));
        return regattaRepository.findAllByCategoryContainsIgnoreCase(category, page);
    }
    public Page<Regatta> getRegattaByDateAndCategoryContainsIgnoreCase(Date startDate, Date endDate, String category, String size) {
        Pageable page = PageRequest.of(0, Integer.parseInt(size));
        return regattaRepository.findAllByDateBetweenAndCategoryContainsIgnoreCase(startDate, endDate, category, page);
    }

    public Page<Regatta> getRegattaByStartDateAndCategoryContainsIgnoreCase(Date startDate, String category, String size) {
        Pageable page = PageRequest.of(0, Integer.parseInt(size));
        return regattaRepository.findAllByDateAfterAndCategoryContainsIgnoreCase(startDate, category, page);
    }

    public Page<Regatta> getRegattaByEndDateAndCategoryContainsIgnoreCase(Date endDate, String category, String size) {
        Pageable page = PageRequest.of(0, Integer.parseInt(size));
        return regattaRepository.findAllByDateBeforeAndCategoryContainsIgnoreCase(endDate, category, page);
    }

    public Regatta getRegatta(Long id) {
        return regattaRepository.findById(id)
                .orElseThrow(() -> new ServiceException("error", "Regatta with id " + id + " not found"));
    }


    public Regatta createRegatta(RegattaDto regatta) {
        Regatta newRegatta = new Regatta();

        newRegatta.setName(regatta.getName());
        newRegatta.setOrganizingClub(regatta.getOrganizingClub());
        newRegatta.setDate(regatta.getDate());
        newRegatta.setMaxTeams(regatta.getMaxTeams());
        newRegatta.setCategory(regatta.getCategory());

        return regattaRepository.save(newRegatta);
    }

    public Regatta updateRegatta(Long id, RegattaDto regatta) {
        Regatta newRegatta = getRegatta(id);

        if (newRegatta.getTeams().size() > 0)
            throw new ServiceException("error", "regatta.has.teams");

        newRegatta.setOrganizingClub(regatta.getOrganizingClub());
        newRegatta.setDate(regatta.getDate());
        newRegatta.setMaxTeams(regatta.getMaxTeams());
        newRegatta.setCategory(regatta.getCategory());

        return regattaRepository.save(newRegatta);
    }

    public void deleteRegatta(Long id) {
        Regatta regatta = getRegatta(id);

        if (regatta.getTeams().size() > 0)
            throw new ServiceException("error", "regatta.has.teams");

        regattaRepository.deleteById(id);
    }

    public Team addTeamToRegatta(Long regattaId, Long teamId) {
        Regatta regatta = getRegatta(regattaId);
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ServiceException("add", "no.team.with.this.id"));

        regatta.addTeam(team);
        regattaRepository.save(regatta);
        return team;
    }

    public Team removeTeamFromRegatta(Long regattaId, Long teamId) {
        Regatta regatta = getRegatta(regattaId);
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ServiceException("remove", "no.team.with.this.id"));

        regatta.removeTeam(team);
        regattaRepository.save(regatta);
        return team;
    }

    public Set<Team> getTeamsFromRegatta(Long regattaId) {
        Regatta regatta = getRegatta(regattaId);
        return regatta.getTeams();
    }

}
