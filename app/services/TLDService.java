package services;

import models.TLD;

import java.util.List;

public interface TLDService {
    /**
     * Get a list of all Top Level Domains in the database (which should have all valid Top Level Domains).
     *
     * @return a {@link List<TLD>} of containing all {@link TLD} objects in the database.
     */
    List<TLD> getAllTLDs();
}
