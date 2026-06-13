package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.PravnickaOsoba;
import cz.vse.java.listingsapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.TypedQuery;
import org.hibernate.exception.JDBCConnectionException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

/**
 * Service for handling user-related operations.
 * @author Adam Filinger
 * @version 1.5
 */
public class UserService {

    private final JPAProvider jpaProvider;
    private final ListingService listingService;
    private final OfferService offerService;

    public UserService() {
        this.listingService = new ListingService();
        this.offerService = new OfferService();
        this.jpaProvider = JPAProvider.getInstance();
    }

    public boolean userExists(String username, String email) {
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
        try {
            String hashedPassword = BCrypt.hashpw(user.getPasswd(), BCrypt.gensalt());
            user.setPasswd(hashedPassword);
            jpaProvider.withTransaction(em -> em.persist(user));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Uzivatel login(String identifier, String password) {
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

    public boolean isBusiness(Uzivatel user) {
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

    public void saveBusiness(PravnickaOsoba pravnickaOsoba) throws Exception {
        if (pravnickaOsoba == null) {
            return;
        }
        jpaProvider.withTransaction(em -> em.persist(pravnickaOsoba));
    }

    public PravnickaOsoba getBusinessForUser(Uzivatel user) {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<PravnickaOsoba> query = em.createQuery(
                    "SELECT p FROM PravnickaOsoba p WHERE p.uzivatel = :user", PravnickaOsoba.class);
            query.setParameter("user", user);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Uzivatel updateUser(Uzivatel user, PravnickaOsoba business) throws Exception {
        try{
            jpaProvider.withTransaction(em -> {
                em.merge(user);
                if (business != null) {
                    em.merge(business);
                }
            });
        } catch (OptimisticLockException e){
            throw new OptimisticLockException("Optimistic lock exception occurred while updating user: " + user.getId() + ".", e);
        } catch (JDBCConnectionException e){
            throw new JDBCConnectionException("JDBC connection exception occurred while updating user: " + user.getId() + ".", e.getSQLException());
        }
        return getUser(user);

    }

    public void deleteUser(Uzivatel user) throws Exception {
        try{
            jpaProvider.withTransaction(em -> {
                Uzivatel managedUser = em.merge(user);
                em.remove(managedUser);
            });
        } catch (OptimisticLockException e){
            throw new OptimisticLockException("Optimistic lock exception occurred while updating user: " + user.getId() + ".", e);
        } catch (JDBCConnectionException e){
            throw new JDBCConnectionException("JDBC connection exception occurred while updating user: " + user.getId() + ".", e.getSQLException());
        }
    }

    public Uzivatel getUser(Uzivatel user){
        try (EntityManager em = jpaProvider.getEntityManager();){
            return em.find(Uzivatel.class, user.getId());
        }
    }


}
