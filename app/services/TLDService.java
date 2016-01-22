package services;

import models.TLD;

import java.util.List;

public interface TLDService {
    //return all TLDs in the DB.
    List<TLD> getAllTLDs();
}
