package cz.vse.java.listingsapp.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the composite key for the PoptavkaTag entity.
 * @author Adam Filinger
 * @version 1.0
 */
@Embeddable
public class PoptavkaTagId implements Serializable {

    private int poptavkaId;
    private int tagId;

    // Getters, Setters, equals, and hashCode

    /**
     * @return the id of the listing
     */
    public int getPoptavkaId() {
        return poptavkaId;
    }

    /**
     * @param poptavkaId the id of the listing to set
     */
    public void setPoptavkaId(int poptavkaId) {
        this.poptavkaId = poptavkaId;
    }

    /**
     * @return the id of the tag
     */
    public int getTagId() {
        return tagId;
    }

    /**
     * @param tagId the id of the tag to set
     */
    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoptavkaTagId that = (PoptavkaTagId) o;
        return poptavkaId == that.poptavkaId && tagId == that.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(poptavkaId, tagId);
    }
}
