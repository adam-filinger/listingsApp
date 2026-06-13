package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Represents an offer made by a user for a listing.
 * @author Adam Filinger
 * @version 1.1
 */
@Entity
@Table(name = "nabidka")
public class Nabidka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "poptavka_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Poptavka poptavka;

    @ManyToOne
    @JoinColumn(name = "uzivatel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Uzivatel uzivatel;

    @Lob
    private String text;

    @Column(name = "proposed_price")
    private double proposedPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusNabidky status;

    @Version
    private int version;

    // Getters and Setters

    /**
     * @return the id of the offer
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
     * @return the listing for which the offer was made
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
     * @return the user who made the offer
     */
    public Uzivatel getUzivatel() {
        return uzivatel;
    }

    /**
     * @param uzivatel the user to set
     */
    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }

    /**
     * @return the text of the offer
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the proposed price of the offer
     */
    public double getProposedPrice() {
        return proposedPrice;
    }

    /**
     * @param proposedPrice the proposed price to set
     */
    public void setProposedPrice(double proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    /**
     * @return the status of the offer
     */
    public StatusNabidky getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusNabidky status) {
        this.status = status;
    }
}
