package cz.vse.java.listingsapp.model;

import jakarta.persistence.*;

/**
 * Represents a user of the application.
 * @author Adam Filinger
 * @version 1.0
 */
@Entity
@Table(name = "uzivatel")
public class Uzivatel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwd;

    // Getters and Setters

    /**
     * @return the id of the user
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
     * @return the name of the user
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
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the email of the user
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
     * @return the password of the user
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * @param passwd the password to set
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
