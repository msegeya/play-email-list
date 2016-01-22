package services;

import models.Address;

import java.util.List;

/**
 * Interface for the Address service data base connection.
 */
public interface AddressService {

    /**
     * Method to insert an an address into the database.
     */
    boolean addAddress(Address address);

    /**
     * Method to retrieve all the addresses in the database.
     */
    List<Address> getAllAddresses();
}
