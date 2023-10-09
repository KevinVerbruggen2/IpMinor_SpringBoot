package be.ucll.ipminor341t.domain;

import be.ucll.ipminor341t.generic.ServiceException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import javax.sql.rowset.serial.SerialException;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category"}))
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private int passengers;
    private String club;

    @ManyToMany(mappedBy = "teams")
    @JsonBackReference
    private Set<Regatta> regattas;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public Set<Regatta> getRegattas() {
        return regattas == null ? regattas = new HashSet<>() : regattas;
    }

    public Set<String> getRegattasNames() {
        return regattas == null ? new HashSet<>() : regattas.stream().map(Regatta::getName).collect(java.util.stream.Collectors.toSet());
    }

    public void addRegatta(Regatta regatta) throws ServiceException {

        boolean isDuplicateDate = this.getRegattas().stream().anyMatch(r -> r.getDate().equals(regatta.getDate()));
        if (isDuplicateDate) throw new ServiceException("error", "You can't add a regatta with the same date as another regatta");

        if (!regatta.getCategory().equals(this.getCategory()))
            throw new ServiceException("error", "You can't add a regatta with a different category than the team");




        this.getRegattas().add(regatta);
    }

    public void removeRegatta(Regatta regatta) {
        this.getRegattas().remove(regatta);
    }


}
