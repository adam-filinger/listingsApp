package cz.vse.java.listingsapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAProvider {

    private static final String PERSISTENCE_UNIT_NAME = "listingsApp";
    private static volatile JPAProvider instance;
    private final EntityManagerFactory emf;

    private JPAProvider() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        // Add a shutdown hook to close the EntityManagerFactory
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

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

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
