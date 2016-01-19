package models;

import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Data model to hold an email address object.
 */

@Entity
public class Address {

    @Id
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Address() { // empty constructor for Spring.

    }

    public String toString() { // toString method because they are your friend.
        return "Address: " + address;
    }
}
