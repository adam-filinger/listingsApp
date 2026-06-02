package cz.vse.java.listingsapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Persistence;
import java.util.function.Consumer;

/**
 * Provides a singleton instance of the JPA EntityManagerFactory and handles transactions.
 * @author Adam Filinger
 * @version 1.1
 */
public class JPAProvider {

    private static final String PERSISTENCE_UNIT_NAME = "listingsApp";
    private static volatile JPAProvider instance;
    private final EntityManagerFactory emf;

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private JPAProvider() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * Returns the singleton instance of the JPAProvider.
     * @return the singleton instance of the JPAProvider
     */
    public static JPAProvider getInstance() {
        if (instance == null) {
            synchronized (JPAProvider.class) {
                if (instance == null) {
                    instance = new JPAProvider();
                }
            }
        }
        return instance;
    }

    /**
     * Returns a new EntityManager instance.
     * @return a new EntityManager instance
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Executes a block of code within a JPA transaction.
     * @param action The block of code to execute.
     */
    public void withTransaction(Consumer<EntityManager> action) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            action.accept(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            if (e instanceof OptimisticLockException){
                throw new OptimisticLockException("Optimistic lock failed", e);
            } else{
                // Re-throw or handle the exception as needed
                throw new RuntimeException("Transaction failed", e);
            }
        } finally {
            em.close();
        }
    }

    /**
     * Closes the EntityManagerFactory.
     */
    private void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
