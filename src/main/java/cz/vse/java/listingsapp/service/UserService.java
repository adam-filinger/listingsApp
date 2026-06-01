package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.PravnickaOsoba;
import cz.vse.java.listingsapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Service for handling user-related operations.
 * @author Adam Filinger
 * @version 1.3
 */
public class UserService {

    private final JPAProvider jpaProvider;

    /**
     * Constructor for UserService.
     */
    public UserService() {
        this.jpaProvider = JPAProvider.getInstance();
    }

    /**
     * Checks if a user with the given username or email already exists.
     * @param username The username to check.
     * @param email The email to check.
     * @return true if a user with the given username or email already exists, false otherwise.
     */
    public boolean userExists(String username, String email) {
        if (username == null || username.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            return false;
        }
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM Uzivatel u WHERE u.username = :username OR u.email = :email", Long.class);
            query.setParameter("username", username);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Saves a new user to the database.
     * Hashes the user's password before saving.
     * @param user The user to save.
     * @return true if the user was saved successfully, false otherwise.
     */
    public boolean saveUser(Uzivatel user) {
        if (user == null || user.getPasswd() == null) {
            return false;
        }
        EntityManager em = jpaProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            String hashedPassword = BCrypt.hashpw(user.getPasswd(), BCrypt.gensalt());
            user.setPasswd(hashedPassword);
            em.persist(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Propagate critical database errors
            throw new RuntimeException("Error saving user", e);
        } finally {
            em.close();
        }
    }

    /**
     * Logs in a user with the given identifier (username or email) and password.
     * @param identifier The username or email of the user.
     * @param password The password of the user.
     * @return The user if the login was successful, null otherwise.
     */
    public Uzivatel login(String identifier, String password) {
        if (identifier == null || identifier.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Uzivatel> query = em.createQuery(
                    "SELECT u FROM Uzivatel u WHERE u.username = :identifier OR u.email = :identifier", Uzivatel.class);
            query.setParameter("identifier", identifier);
            Uzivatel user = query.getSingleResult();

            if (BCrypt.checkpw(password, user.getPasswd())) {
                return user;
            }
        } catch (NoResultException e) {
            // User not found
        } finally {
            em.close();
        }
        return null;
    }

    /**
     * Checks if a user is registered as a business.
     * @param user The user to check.
     * @return true if the user is registered as a business, false otherwise.
     */
    public boolean isBusiness(Uzivatel user) {
        if (user == null) {
            return false;
        }
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(p) FROM PravnickaOsoba p WHERE p.uzivatel = :user", Long.class);
            query.setParameter("user", user);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Saves a new business to the database.
     * @param pravnickaOsoba The business to save.
     * @return true if the business was saved successfully, false otherwise.
     */
    public boolean saveBusiness(PravnickaOsoba pravnickaOsoba) {
        if (pravnickaOsoba == null) {
            return false;
        }
        EntityManager em = jpaProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pravnickaOsoba);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }
}
