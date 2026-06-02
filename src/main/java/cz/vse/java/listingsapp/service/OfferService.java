package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

/**
 * Service for handling offer-related operations.
 * @author Adam Filinger
 * @version 1.3
 */
public class OfferService {

    private final JPAProvider jpaProvider;

    /**
     * Constructor for OfferService.
     */
    public OfferService() {
        this.jpaProvider = JPAProvider.getInstance();
    }

    /**
     * Saves a new offer to the database.
     * @param nabidka The offer to save.
     */
    public void saveNabidka(Nabidka nabidka) {
        if (nabidka == null) {
            return;
        }
        jpaProvider.withTransaction(em -> em.persist(nabidka));
    }

    /**
     * Updates an existing offer in the database.
     * @param nabidka The offer to update.
     */
    public void updateNabidka(Nabidka nabidka) {
        if (nabidka == null) {
            return;
        }
        jpaProvider.withTransaction(em -> em.merge(nabidka));
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

    /**
     * Retrieves all offers for a given listing made by a specific user.
     * @param poptavka The listing to get offers for.
     * @param user The user who made the offers.
     * @return A list of offers.
     */
    public List<Nabidka> getOffersForListingByUser(Poptavka poptavka, Uzivatel user) {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Nabidka> query = em.createQuery(
                    "SELECT n FROM Nabidka n WHERE n.poptavka = :poptavka AND n.uzivatel = :user ORDER BY n.id DESC", Nabidka.class);
            query.setParameter("poptavka", poptavka);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }
}
