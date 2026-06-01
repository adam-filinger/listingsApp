package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;

/**
 * Represents a legal entity.
 * @author Adam Filinger
 * @version 1.0
 */
@Entity
@Table(name = "pravnicka_osoba")
public class PravnickaOsoba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String ico;

    @OneToOne
    @JoinColumn(name = "uzivatel_id", referencedColumnName = "id")
    private Uzivatel uzivatel;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String tel;

    // Getters and Setters

    /**
     * @return the id of the legal entity
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
     * @return the ICO of the legal entity
     */
    public String getIco() {
        return ico;
    }

    /**
     * @param ico the ICO to set
     */
    public void setIco(String ico) {
        this.ico = ico;
    }

    /**
     * @return the user associated with the legal entity
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
     * @return the name of the legal entity
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
     * @return the email of the legal entity
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the telephone number of the legal entity
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the telephone number to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }
}
