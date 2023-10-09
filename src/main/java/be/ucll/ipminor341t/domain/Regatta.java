package be.ucll.ipminor341t.domain;

import be.ucll.ipminor341t.generic.ServiceException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "regatta", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "organizingClub", "date"}))
public class Regatta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String organizingClub;
    private Date date;
    private String maxTeams;
    private String category;

    @ManyToMany
    @JoinTable(
            name = "team_regatta",
            joinColumns = @JoinColumn(name = "regatta_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @JsonManagedReference
    private Set<Team> teams;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getOrganizingClub() {
        return organizingClub;
    }

    public Date getDate() {
        return date;
    }
    public String getDateStr() {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    public String getMaxTeams() {
        return maxTeams;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganizingClub(String organizingClub) {
        this.organizingClub = organizingClub;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMaxTeams(String maxTeams) {
        this.maxTeams = maxTeams;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Team> getTeams() {
        return teams == null ? teams = new HashSet<>() : teams;
    }


    public void addTeam(Team team) {

        if (getTeams().size() >= Integer.parseInt(maxTeams))
            throw new ServiceException("error", "You can't add a regatta with more teams than the maxTeams");

        team.addRegatta(this);
        teams.add(team);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
        team.removeRegatta(this);
    }
}