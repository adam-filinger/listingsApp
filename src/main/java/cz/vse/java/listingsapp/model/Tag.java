package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;

/**
 * Represents a tag that can be associated with a listing.
 * @author Adam Filinger
 * @version 1.0
 */
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
