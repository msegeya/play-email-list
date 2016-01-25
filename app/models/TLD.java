package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Data model to hold a Top Level Domain for comparison by the app.
 * Common examples of TLDs are: .com, .org, .net
 */
@Entity
@Table(name = "tlds")
public class TLD {

    @Id
    @Column(name = "tld")
    private String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String toString() { // toString method because they are your friend.
        return "TLD: " + domain;
    }

    @Override
    public int hashCode() {
        return domain.hashCode();
    }

    @Override
    /**
     * Compares to other {@link TLD} based only on the 'domain' string.
     * @Return {@link Boolean}, {@link Boolean#TRUE} iff the 'domain' strings of both objects match.
     */
    public boolean equals(Object object) {
        boolean isEqual = false;
        if (object == null) {
            return false;
        } else if (object instanceof TLD) {
            isEqual = (this.domain.equals(((TLD)object).domain));
        } else if (object instanceof String) {
            isEqual = (this.domain.equals(object));
        }
        return isEqual;
    }

}
