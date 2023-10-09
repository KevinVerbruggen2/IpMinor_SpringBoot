package be.ucll.ipminor341t.domain;

import be.ucll.ipminor341t.generic.ServiceException;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "storage", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "place"}))
public class Storage {

    @OneToMany(mappedBy = "storage")
    @JsonManagedReference
    private Set<Boat> boats;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String place;
    private String availableSpace;
    private String height;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<Boat> getBoats() {
        return boats == null ? boats = new HashSet<>() : boats;
    }

    public void addBoat(Boat boat) {
        if(Double.parseDouble(this.height) <= boat.getHeight()) throw new ServiceException("storageError", "Cannot add boat to storage because boat is to high");

        double totalArea = this.getBoats().stream().mapToDouble(boat1 -> boat1.getWidth() * boat1.getLength()).sum() + (boat.getLength()*boat.getWidth());
        if(totalArea > (Double.parseDouble(this.availableSpace)) * 0.80) throw new ServiceException("storageError", "Boat can't be added because storage is full");

        boolean isDuplicateEmail = this.getBoats().stream().anyMatch(b -> b.getEmail().equals(boat.getEmail()));
        if(isDuplicateEmail) throw new ServiceException("storageError", "You can't add another boat from the same owner");

        boat.setStorage(this);
        getBoats().add(boat);
    }

    public void removeBoat(Boat boat) {
        boat.setStorage(null);
        getBoats().remove(boat);
    }
}

