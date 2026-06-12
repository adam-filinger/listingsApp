package cz.vse.java.listingsapp.service;

import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.TypedQuery;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

/**
 * Service for handling listing-related operations.
 * @author Adam Filinger
 * @version 1.8
 */
public class ListingService {

    private Logger logger =  LoggerFactory.getLogger(this.getClass());
    private final JPAProvider jpaProvider;

    public ListingService() {
        this.jpaProvider = JPAProvider.getInstance();
    }

    public void savePoptavka(Poptavka poptavka) throws JDBCConnectionException {
        if (poptavka == null) {
            return;
        }
        try{
            jpaProvider.withTransaction(em -> em.persist(poptavka));
        } catch (Exception e){
            logger.warn(e.getMessage());
            if(e instanceof JDBCConnectionException sqlException) {
                throw new JDBCConnectionException("Database connection error while saving listing", sqlException.getSQLException());
            }
        }

    }

    public void updatePoptavka(Poptavka poptavka) throws OptimisticLockException {
        if (poptavka == null) {
            return;
        }
        try{
            jpaProvider.withTransaction(em -> em.merge(poptavka));
        } catch (Exception e){
            if(e instanceof OptimisticLockException){
                throw new OptimisticLockException("Listing was modified by another transaction. Please reload and try again.", e);
            }
            if(e instanceof JDBCConnectionException sqlException){
                throw new JDBCConnectionException("Database connection error while updating listing", sqlException.getSQLException());
            }
        }
    }

    public Poptavka findPoptavkaById(int id) {
        EntityManager em = jpaProvider.getEntityManager();
        try {
            return em.find(Poptavka.class, id);
        } finally {
            em.close();
        }
    }

    public List<Poptavka> getListings() throws JDBCConnectionException{
        EntityManager em = jpaProvider.getEntityManager();
        try {
            TypedQuery<Poptavka> query = em.createQuery(
                        "SELECT p FROM Poptavka p ORDER BY p.createdDate DESC", Poptavka.class);

            return query.getResultList();
        } catch (Exception e) {
            logger.warn("Error while fetching listings: {}",e.getMessage());
            if(e instanceof JDBCConnectionException sqlException) {
                throw new JDBCConnectionException("Database connection error while retrieving listings", sqlException.getSQLException());
            }
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
}
