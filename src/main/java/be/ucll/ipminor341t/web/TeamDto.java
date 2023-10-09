package be.ucll.ipminor341t.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class TeamDto {
    private long id;

    @NotBlank(message = "name.missing")
    @Pattern(regexp = "^$|^.{5,}$", message = "name.too.short")
    private String name;

    @NotBlank(message = "category.missing")
    @Pattern(regexp = "^$|^[a-zA-Z0-9]{7}$", message = "category.pattern")
    private String category;

    @Min(value = 1, message = "passengers.min")
    @Max(value = 12, message = "passengers.max")
    private int passengers;

    private String club;

    private List<RegattaDto> regattas;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int numberOfCrew) {
        this.passengers = numberOfCrew;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public List<RegattaDto> getRegattas() {
        return regattas;
    }

    public void setReggatas(List<RegattaDto> regattas) {
        this.regattas = regattas;
    }
}