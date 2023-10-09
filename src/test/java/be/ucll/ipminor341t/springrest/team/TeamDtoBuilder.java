package be.ucll.ipminor341t.springrest.team;

import be.ucll.ipminor341t.web.TeamDto;

public class TeamDtoBuilder {

    private String name;
    private String category;
    private int numberOfCrew;
    private String club;

    private TeamDtoBuilder() {
    }

    public static TeamDtoBuilder aTeam () {
        return new TeamDtoBuilder();
    }

    public static TeamDtoBuilder aTeamA () {
        return aTeam().withName("Team P").withCategory("1238067").withNumberOfCrew(4).withClub("YTR Sailing Club");
    }

    public static TeamDtoBuilder aTeamB () {
        return aTeam().withName("Team B").withCategory("1235067").withNumberOfCrew(4).withClub("YTR Sailing Club");
    }

    public static TeamDtoBuilder anInvalidTeamWithNoName() {
        return aTeam().withName("").withCategory("4568536").withNumberOfCrew(4).withClub("ABC Sailing Club");
    }

    public static TeamDtoBuilder anInvalidTeamWithNameWith3Characters() {
        return aTeam().withName("ABC").withCategory("4568536").withNumberOfCrew(4).withClub("ABC Sailing Club");
    }

    public TeamDtoBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public TeamDtoBuilder withCategory (String category) {
        this.category = category;
        return this;
    }

    public static TeamDtoBuilder anInvalidTeamWithNoCategory() {
        return aTeam().withName("Team P").withCategory("").withNumberOfCrew(4).withClub("YTR Sailing Club");
    }

    public static TeamDtoBuilder anInvalidTeamWithCategoryShorterThan7Characters(){
        return aTeam().withName("Team P").withCategory("catego").withNumberOfCrew(4).withClub("YTR Sailing Club");
    }


    public TeamDtoBuilder withNumberOfCrew (int numberOfCrew) {
        this.numberOfCrew = numberOfCrew;
        return this;
    }

    public static TeamDtoBuilder anInvalidTeamWithNumberOfCrewLessThan1() {
        return aTeam().withName("Team P").withCategory("1238067").withNumberOfCrew(0).withClub("YTR Sailing Club");
    }
    public static TeamDtoBuilder anInvalidTeamWithNumberOfCrewMoreThan12() {
        return aTeam().withName("Team P").withCategory("1238067").withNumberOfCrew(13).withClub("YTR Sailing Club");
    }

    public TeamDtoBuilder withClub (String club) {
        this.club = club;
        return this;
    }

    public TeamDto build() {
        TeamDto team = new TeamDto();
        team.setName(name);
        team.setCategory(category);
        team.setPassengers(numberOfCrew);
        team.setClub(club);
        return team;
    }
}
