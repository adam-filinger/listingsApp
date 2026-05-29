package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final JPAProvider jpaProvider;

    public UserService() {
        this.jpaProvider = JPAProvider.getInstance();
    }

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
}
