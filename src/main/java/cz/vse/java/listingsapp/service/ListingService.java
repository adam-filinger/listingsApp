package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Collections;

/**
 * Service for handling listing-related operations.
 * @author Adam Filinger
 * @version 1.1
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
}
