package cz.vse.java.listingsapp.controller;

/**
 * An interface for controllers that can be refreshed.
 * @author Adam Filinger
 * @version 1.0
 */
public interface Refresheable {
    /**
     * Refreshes the content of the view.
     */
    void refresh();
}
