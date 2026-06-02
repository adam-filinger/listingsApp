package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.Category;
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
 * @version 1.8
 */
public class ListingService {

    private final JPAProvider jpaProvider;

    public ListingService() {
        this.jpaProvider = JPAProvider.getInstance();
    }

    public void savePoptavka(Poptavka poptavka) {
        if (poptavka == null) {
            return;
        }
        jpaProvider.withTransaction(em -> em.persist(poptavka));
    }
    
    public void updatePoptavka(Poptavka poptavka) throws OptimisticLockException {
        if (poptavka == null) {
            return;
        }
        jpaProvider.withTransaction(em -> em.merge(poptavka));
    }

    public Poptavka findPoptavkaById(int id) {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            return em.find(Poptavka.class, id);
        } finally {
            em.close();
        }
    }

    public List<Poptavka> getListings() {
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

    public List<Poptavka> getListings(Uzivatel user) {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Poptavka> query = em.createQuery(
                    "SELECT p FROM Poptavka p WHERE p.pravnickaOsoba.uzivatel = :user ORDER BY p.createdDate DESC", Poptavka.class);
            query.setParameter("user", user);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    public List<Poptavka> getListings(Category category) {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Poptavka> query = em.createQuery(
                    "SELECT p FROM Poptavka p WHERE p.category = :category ORDER BY p.createdDate DESC", Poptavka.class);
            query.setParameter("category", category);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }
}
