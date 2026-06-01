package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Represents a listing created by a legal entity.
 * @author Adam Filinger
 * @version 1.0
 */
@Entity
@Table(name = "poptavka")
public class Poptavka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "pravnicka_osoba_id", referencedColumnName = "id")
    private PravnickaOsoba pravnickaOsoba;

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    private double price;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    // Getters and Setters

    /**
     * @return the id of the listing
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the legal entity that created the listing
     */
    public PravnickaOsoba getPravnickaOsoba() {
        return pravnickaOsoba;
    }

    /**
     * @param pravnickaOsoba the legal entity to set
     */
    public void setPravnickaOsoba(PravnickaOsoba pravnickaOsoba) {
        this.pravnickaOsoba = pravnickaOsoba;
    }

    /**
     * @return the name of the listing
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description of the listing
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the price of the listing
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the creation date of the listing
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the creation date to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
