package services;

import java.util.List;

import models.TLD;


public interface TLDService {
    //return all TLDs in the DB.
    List<TLD> getAllTLDs();
}
