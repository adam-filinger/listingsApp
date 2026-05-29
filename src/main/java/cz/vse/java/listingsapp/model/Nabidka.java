package cz.vse.java.listingsapp.model;

public class Nabidka {
    private int id;
    private int poptavkaId;
    private int uzivatelId;
    private String text;
    private double proposedPrice;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoptavkaId() {
        return poptavkaId;
    }

    public void setPoptavkaId(int poptavkaId) {
        this.poptavkaId = poptavkaId;
    }

    public int getUzivatelId() {
        return uzivatelId;
    }

    public void setUzivatelId(int uzivatelId) {
        this.uzivatelId = uzivatelId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
