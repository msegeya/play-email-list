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
     * Method will return FALSE if the insert fails.
     *
     * @Return {@link Boolean}, {@link Boolean#TRUE} if the insert succeeds and {@link Boolean#FALSE} otherwise.
     */
    boolean addAddress(Address address);

    /**
     * Method to retrieve all the addresses in the database.
     *
     * @Return A {@link List<Address>} containing all the {@link Address} Objects in the database.
     * List will be empty if the database is empty.
     */
    List<Address> getAllAddresses();
}
