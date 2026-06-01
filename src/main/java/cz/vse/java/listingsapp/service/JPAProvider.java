package cz.vse.java.listingsapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Provides a singleton instance of the JPA EntityManagerFactory.
 * @author Adam Filinger
 * @version 1.0
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
        // Add a shutdown hook to close the EntityManagerFactory
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
     * Closes the EntityManagerFactory.
     */
    private void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
