package services;

import models.Address;

import java.util.List;

/**
 * Defines methods needed to fetch and persist {@link Task tasks}.
 *
 * @author afrieze
 *
 */
public interface AddressService {

    /**
     * Adds the {@link Task} to the persistence strategy such that a future call to {@link #getAllTasks()} will contain the passed
     * in task.
     *
     * @param task
     */
    public void addAddress(Address address);

    /**
     * Returns all {@link tasks} known to this service. Note that any task added through the {@link #addTask(Task)} method should be
     * present in the resulting list.
     *
     * @return the {@Task tasks} known to the service,
     */
    public List<Address> getAllAddresses();

}