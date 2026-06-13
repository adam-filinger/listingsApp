package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.StatusNabidky;
import cz.vse.java.listingsapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.TypedQuery;
import org.hibernate.exception.JDBCConnectionException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    public void saveNabidka(Nabidka nabidka) throws Exception {
        if (nabidka == null) {
            return;
        }
        jpaProvider.withTransaction(em -> em.persist(nabidka));
    }

    /**
     * Updates an existing offer in the database.
     * @param nabidka The offer to update.
     */
    public Nabidka updateNabidka(Nabidka nabidka) throws Exception {
        if (nabidka == null) {
            return null;
        }
        try{
            jpaProvider.withTransaction(em -> em.merge(nabidka));
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("Optimistic lock exception occurred while updating offer: " + nabidka.getId() + ".", e);
        }
        return getOfferById(nabidka);
    }


    public Nabidka getOfferById(Nabidka offer){
        try (EntityManager em = jpaProvider.getEntityManager()) {
            return em.find(Nabidka.class, offer.getId());
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

    public boolean isAccepted(Nabidka offer) {
        try(EntityManager em = jpaProvider.getEntityManager()){
            return Objects.equals(StatusNabidky.PRIJATA, em.find(Nabidka.class, offer.getId()).getStatus());
        }
    }

    public void deleteOffer(Nabidka offer) throws Exception {
        if (offer == null) {
            return;
        }
        try {
            jpaProvider.withTransaction(em -> {
                Nabidka managedOffer = em.merge(offer);
                em.remove(managedOffer);
            });
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("Optimistic lock exception occurred while deleting offer: " + offer.getId() + ".", e);
        } catch (JDBCConnectionException e) {
            throw new JDBCConnectionException("JDBC connection exception occurred while deleting offer: " + offer.getId() + ".", e.getSQLException());
        } catch (Exception e) {
            throw new Exception("Failed to delete offer: " + offer.getId() + ".", e);
        }
    }

}
