package services;

import models.Address;

import java.util.List;

/**
 * Interface for the Address database connection.
 */
public interface AddressService {

    /**
     * Method to try to insert an an address into the database.
     * <p>
     *     Method will return FALSE if the insert fails.
     *
     * @Return True if the insert succeeds and False otherwise.
     */
    boolean addAddress(Address address);

    /**
     * Method to retrieve all the addresses in the database.
     *
     * @Return A List containing all the Address Objects in the database. List will be empty if the database is empty.
     */
    List<Address> getAllAddresses();
}
