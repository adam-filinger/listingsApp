package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;
import java.util.Date;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PravnickaOsoba getPravnickaOsoba() {
        return pravnickaOsoba;
    }

    public void setPravnickaOsoba(PravnickaOsoba pravnickaOsoba) {
        this.pravnickaOsoba = pravnickaOsoba;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
