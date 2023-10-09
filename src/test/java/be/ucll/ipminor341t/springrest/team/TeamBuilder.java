package be.ucll.ipminor341t.springrest.team;

import be.ucll.ipminor341t.domain.Team;

public class TeamBuilder {
    private String name;
    private String category;
    private int numberOfCrew;
    private String club;

    private TeamBuilder() {}

    public static TeamBuilder aTeam () {
        return new TeamBuilder();
    }

    public static TeamBuilder aTeamA () {
        return aTeam().withName("Team P").withCategory("1238067").withNumberOfCrew(4).withClub("YTR Sailing Club");
    }

    public static TeamBuilder aTeamB () {
        return aTeam().withName("Team B").withCategory("1235067").withNumberOfCrew(4).withClub("YTR Sailing Club");
    }

    public static TeamBuilder aTeamC() {
        return aTeam().withName("Team C").withCategory("7654321").withNumberOfCrew(4).withClub("ABC Sailing Club");
    }

    public static TeamBuilder anInvalidTeamWithNoName() {
        return aTeam().withName("").withCategory("7654321").withNumberOfCrew(4).withClub("HGFf Sailing Club");
    }

    public TeamBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public TeamBuilder withCategory (String category) {
        this.category = category;
        return this;
    }

    public TeamBuilder withNumberOfCrew (int numberOfCrew) {
        this.numberOfCrew = numberOfCrew;
        return this;
    }

    public TeamBuilder withClub (String club) {
        this.club = club;
        return this;
    }

    public Team build() {
        Team team = new Team();
        team.setName(name);
        team.setCategory(category);
        team.setPassengers(numberOfCrew);
        team.setClub(club);
        return team;
    }
}
