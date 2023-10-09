package be.ucll.ipminor341t.web;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class RegattaDto {
    private long id;

    @NotBlank(message = "name.missing")
    private String name;

    @NotBlank(message = "club.missing")
    private String organizingClub;

    @NotNull(message = "date.missing")
    @Future(message = "date.future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;


    @NotBlank(message = "maxTeams.missing")
    @Pattern(regexp = "^$|^[1-9]\\d*$", message = "maxTeams.positive")
    private String maxTeams;

    @NotBlank(message = "category.missing")
    private String category;

    private List<TeamDto> teams;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizingClub() {
        return organizingClub;
    }

    public void setOrganizingClub(String organizingClub) {
        this.organizingClub = organizingClub;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(String maxTeams) {
        this.maxTeams = maxTeams;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<TeamDto> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDto> teams) {
        this.teams = teams;
    }
}
