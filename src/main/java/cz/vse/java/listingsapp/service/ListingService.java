package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Collections;

/**
 * Service for handling listing-related operations.
 * @author Adam Filinger
 * @version 1.4
 */
public class ListingService {

    private final JPAProvider jpaProvider;

    /**
     * Constructor for ListingService.
     */
    public ListingService() {
        this.jpaProvider = JPAProvider.getInstance();
    }

    /**
     * Saves a new listing to the database.
     * @param poptavka The listing to save.
     * @return true if the listing was saved successfully, false otherwise.
     */
    public boolean savePoptavka(Poptavka poptavka) {
        if (poptavka == null) {
            return false;
        }
        EntityManager em = jpaProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(poptavka);
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
    
    /**
     * Updates an existing listing in the database.
     * This method uses optimistic locking.
     * @param poptavka The listing with updated data.
     * @throws OptimisticLockException if the listing has been updated by another transaction.
     */
    public void updatePoptavka(Poptavka poptavka) throws OptimisticLockException {
        if (poptavka == null) {
            return;
        }
        EntityManager em = jpaProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(poptavka);
            em.getTransaction().commit();
        } catch (OptimisticLockException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Re-throw the exception to be handled by the controller
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Optionally, wrap in a custom service exception
        } finally {
            em.close();
        }
    }

    /**
     * Finds a listing by its ID.
     * @param id The ID of the listing to find.
     * @return The found listing, or null if not found.
     */
    public Poptavka findPoptavkaById(int id) {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            return em.find(Poptavka.class, id);
        } finally {
            em.close();
        }
    }


    /**
     * Saves a new offer to the database.
     * @param nabidka The offer to save.
     * @return true if the offer was saved successfully, false otherwise.
     */
    public boolean saveNabidka(Nabidka nabidka) {
        if (nabidka == null) {
            return false;
        }
        EntityManager em = jpaProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(nabidka);
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

    /**
     * Retrieves all listings from the database, ordered by creation date descending.
     * @return A list of all listings.
     */
    public List<Poptavka> getAllPoptavky() {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Poptavka> query = em.createQuery(
                    "SELECT p FROM Poptavka p ORDER BY p.createdDate DESC", Poptavka.class);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all offers for a given listing.
     * @param poptavka The listing to get offers for.
     * @return A list of offers.
     */
    public List<Nabidka> getOffersForListing(Poptavka poptavka) {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Nabidka> query = em.createQuery(
                    "SELECT n FROM Nabidka n WHERE n.poptavka = :poptavka ORDER BY n.id DESC", Nabidka.class);
            query.setParameter("poptavka", poptavka);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all offers made by a specific user.
     * @param user The user to get offers for.
     * @return A list of offers.
     */
    public List<Nabidka> getOffersByUser(Uzivatel user) {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Nabidka> query = em.createQuery(
                    "SELECT n FROM Nabidka n WHERE n.uzivatel = :user ORDER BY n.id DESC", Nabidka.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }
}
