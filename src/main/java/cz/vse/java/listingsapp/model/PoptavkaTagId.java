package cz.vse.java.listingsapp.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PoptavkaTagId implements Serializable {

    private int poptavkaId;
    private int tagId;

    // Getters, Setters, equals, and hashCode

    public int getPoptavkaId() {
        return poptavkaId;
    }

    public void setPoptavkaId(int poptavkaId) {
        this.poptavkaId = poptavkaId;
    }

    public int getTagId() {
        return tagId;
    }

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
