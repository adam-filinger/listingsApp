package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;

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

    public PoptavkaTagId getId() {
        return id;
    }

    public void setId(PoptavkaTagId id) {
        this.id = id;
    }

    public Poptavka getPoptavka() {
        return poptavka;
    }

    public void setPoptavka(Poptavka poptavka) {
        this.poptavka = poptavka;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
