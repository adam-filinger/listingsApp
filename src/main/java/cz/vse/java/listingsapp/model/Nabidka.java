package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nabidka")
public class Nabidka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "poptavka_id", nullable = false)
    private Poptavka poptavka;

    @ManyToOne
    @JoinColumn(name = "uzivatel_id", nullable = false)
    private Uzivatel uzivatel;

    @Lob
    private String text;

    @Column(name = "proposed_price")
    private double proposedPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusNabidky status;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Poptavka getPoptavka() {
        return poptavka;
    }

    public void setPoptavka(Poptavka poptavka) {
        this.poptavka = poptavka;
    }

    public Uzivatel getUzivatel() {
        return uzivatel;
    }

    public void setUzivatel(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getProposedPrice() {
        return proposedPrice;
    }

    public void setProposedPrice(double proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    public StatusNabidky getStatus() {
        return status;
    }

    public void setStatus(StatusNabidky status) {
        this.status = status;
    }
}
