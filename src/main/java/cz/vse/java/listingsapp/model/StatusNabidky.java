package cz.vse.java.listingsapp.model;

/**
 * Represents the status of an offer.
 * @author Adam Filinger
 * @version 1.0
 */
public enum StatusNabidky {
    /**
     * The offer is new.
     */
    NOVA,
    /**
     * The offer has been accepted.
     */
    PRIJATA,
    /**
     * The offer has been rejected.
     */
    ODMITNUTA,
    /**
     * The offer has been cancelled.
     */
    ZRUSENA
}
