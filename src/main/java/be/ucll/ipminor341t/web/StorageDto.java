package be.ucll.ipminor341t.web;

import be.ucll.ipminor341t.domain.Boat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;


public class StorageDto {
    private Long id;

    private List<Boat> boats;

    @NotBlank(message = "name.missing")
    @Pattern(regexp = "^$|^.{5,}$", message = "name.too.short")
    private String name;
    @NotBlank(message = "place.missing")
    @Pattern(regexp = "^$|\\d{4}", message = "place.invalid")
    private String place;
    @NotBlank(message = "availableSpace.missing")
    @Pattern(regexp = "^$|^[1-9]\\d*([,.]\\d+)?$", message = "availableSpace.must.be.positive")
    private String availableSpace;
    @NotBlank(message = "height.missing")
    @Pattern(regexp = "^$|^[1-9]\\d*([,.]\\d+)?$", message = "height.must.be.positive")
    private String height;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAvailableSpace() {
        return availableSpace;
    }

    public void setAvailableSpace(String availableSpace) {
        this.availableSpace = availableSpace;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public List<Boat> getBoats() {
        return boats;
    }

    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }
}
