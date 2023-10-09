package be.ucll.ipminor341t.domain;

import be.ucll.ipminor341t.generic.ServiceException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "boat", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "email"}), @UniqueConstraint(columnNames = {"insurance"})})
public class Boat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name.missing")
    @Pattern(regexp = "^$|^.{5,}$", message = "name.too.short")
    private String name;

    @NotBlank(message = "email.missing")
    @Pattern(regexp = "^$|^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "email.not.valid")
    private String email;

    @NotBlank(message = "insurance.number.missing")
    @Pattern(regexp = "^$|^[a-zA-Z0-9]{10}$", message = "insurance.number.invalid")
    private String insurance;

    @NotNull(message = "length.missing")
    @Positive(message = "length.must.be.positive")
    private double length;

    @NotNull(message = "width.missing")
    @Positive(message = "width.must.be.positive")
    private double width;

    @NotNull(message = "height.missing")
    @Positive(message = "height.must.be.positive")
    private double height;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    @JsonBackReference
    private Storage storage;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setStorage(Storage storage) {
        if(storage != null && this.storage != null) {
            throw new ServiceException("boatError", "Boat is already added to another storage");
        }
        this.storage = storage;
    }

    public Storage getStorage() {
        return storage;
    }

    public String getStorageName() {
        return storage == null ? "Unknown" : storage.getName();
    }

}
