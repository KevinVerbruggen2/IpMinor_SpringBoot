package be.ucll.ipminor341t.springrest.boat;

import be.ucll.ipminor341t.domain.Boat;

public class BoatBuilder {
    private String name;
    private String email;
    private String insurance;
    private double length;
    private double width;
    private double height;

    private BoatBuilder() {}

    public static BoatBuilder aTeam () {
        return new BoatBuilder();
    }

    public static BoatBuilder aTeamA () {
        return aTeam().withName("Team P").withEmail("SailingClub@gmail.com").withInsurance("1234567890").withLength(4.5).withWidth(2.5).withHeight(1.5);
    }

    public static BoatBuilder aTeamB () {
        return aTeam().withName("Team B").withEmail("CaptainClub@gmail.com").withInsurance("123ABC7890").withLength(5.5).withWidth(10.5).withHeight(1.5);
    }

    public static BoatBuilder aTeamC() {
        return aTeam().withName("Team C").withEmail("CaptainClub@gmail.com").withInsurance("Z23ABC789D").withLength(2.5).withWidth(9.5).withHeight(5.5);
    }

    public static BoatBuilder anInvalidTeamWithNoName() {
        return aTeam().withName("").withEmail("CaptainClub@gmail.com").withInsurance("Z23ABC789D").withLength(2.5).withWidth(9.5).withHeight(5.5);
    }

    public static BoatBuilder anInvalidTeamWithShortName() {
        return aTeam().withName("test").withEmail("CaptainClub@gmail.com").withInsurance("Z23ABC789D").withLength(2.5).withWidth(9.5).withHeight(5.5);
    }

    public static BoatBuilder anInvalidTeamWithNoEmail() {
        return aTeam().withName("Captain Club").withEmail("").withInsurance("Z23ABC789D").withLength(2.5).withWidth(9.5).withHeight(5.5);
    }


    public static BoatBuilder anInvalidTeamWithInvalidEmail() {
        return aTeam().withName("Captain Club").withEmail("CaptainClub#gmail").withInsurance("Z23ABC789D").withLength(2.5).withWidth(9.5).withHeight(5.5);
    }

    public static BoatBuilder anInvalidTeamWithNoInsurance() {
        return aTeam().withName("Captain Club").withEmail("CaptainClub@gmail.com").withInsurance("").withLength(2.5).withWidth(9.5).withHeight(5.5);
    }

    public static BoatBuilder anInvalidTeamWithInvalidInsurance() {
        return aTeam().withName("Captain Club").withEmail("CaptainClub@gmail.com").withInsurance("21EI").withLength(2.5).withWidth(9.5).withHeight(5.5);
    }

    public static BoatBuilder anInvalidTeamWithNegativeLength() {
        return aTeam().withName("Captain Club").withEmail("CaptainClub@gmail.com").withInsurance("ABCDEFGH19").withLength(-1).withWidth(9.5).withHeight(5.5);
    }

    public static BoatBuilder anInvalidTeamWithNegativeWidth() {
        return aTeam().withName("Captain Club").withEmail("CaptainClub@gmail.com").withInsurance("ABCDEFGH19").withLength(2.5).withWidth(-1).withHeight(5.5);
    }

    public static BoatBuilder anInvalidTeamWithNegativeHeight() {
        return aTeam().withName("Captain Club").withEmail("CaptainClub@gmail.com").withInsurance("ABCDEFGH19").withLength(2.5).withWidth(9.5).withHeight(-1);
    }

    public BoatBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public BoatBuilder withEmail (String email) {
        this.email = email;
        return this;
    }

    public BoatBuilder withInsurance (String insurance) {
        this.insurance = insurance;
        return this;
    }

    public BoatBuilder withLength (double length) {
        this.length = length;
        return this;
    }

    public BoatBuilder withWidth (double width) {
        this.width = width;
        return this;
    }

    public BoatBuilder withHeight (double height) {
        this.height = height;
        return this;
    }

    public Boat build() {
        Boat boat = new Boat();
        boat.setName(name);
        boat.setEmail(email);
        boat.setInsurance(insurance);
        boat.setLength(length);
        boat.setWidth(width);
        boat.setHeight(height);
        return boat;
    }
}
