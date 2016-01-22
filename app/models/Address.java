package models;

import javax.persistence.Table;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Data model to hold an email address object.
 */

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() { // toString method because they are your friend.
        return "Address: " + address;
    }
}
