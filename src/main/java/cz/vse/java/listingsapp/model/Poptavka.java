package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * Represents a listing created by a legal entity.
 * @author Adam Filinger
 * @version 1.2
 */
@Entity
@Table(name = "poptavka")
public class Poptavka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "pravnicka_osoba_id", referencedColumnName = "id")
    private PravnickaOsoba pravnickaOsoba;

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    private double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "poptavka_listing_tag",
            joinColumns = @JoinColumn(name = "poptavka_id"),
            inverseJoinColumns = @JoinColumn(name = "listing_tag_id")
    )
    private Set<ListingTag> tags;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<ListingTag> getTags() {
        return tags;
    }

    public void setTags(Set<ListingTag> tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
