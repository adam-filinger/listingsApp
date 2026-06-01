package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;

/**
 * Represents the many-to-many relationship between a listing and a tag.
 * @author Adam Filinger
 * @version 1.0
 */
@Entity
@Table(name = "poptavka_tag")
public class PoptavkaTag {

    @EmbeddedId
    private PoptavkaTagId id;

    @ManyToOne
    @MapsId("poptavkaId")
    @JoinColumn(name = "poptavka_id")
    private Poptavka poptavka;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // Getters and Setters

    /**
     * @return the id of the listing-tag relationship
     */
    public PoptavkaTagId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(PoptavkaTagId id) {
        this.id = id;
    }

    /**
     * @return the listing
     */
    public Poptavka getPoptavka() {
        return poptavka;
    }

    /**
     * @param poptavka the listing to set
     */
    public void setPoptavka(Poptavka poptavka) {
        this.poptavka = poptavka;
    }

    /**
     * @return the tag
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
