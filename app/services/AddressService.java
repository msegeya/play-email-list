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
    public void addAddress(Address address);

    /**
     * Method to retreave all the addresses in the database.
     */
    public List<Address> getAllAddresses();

}
