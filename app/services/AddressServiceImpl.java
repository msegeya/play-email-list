package services;

import models.Address;
import configs.DataConfig;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.EntityExistsException;

@Service
public class AddressServiceImpl implements AddressService {

    @PersistenceContext
    EntityManager em;

    /**
     * Attempt to insert an address into the database. Catch an exception if the entity is already in the database.
     */
    @Override
    @Transactional
    public void addAddress(Address address) throws DataIntegrityViolationException {

        try {
            em.persist(address);
        } catch (EntityExistsException e) {

        }
    }

    /**
     * Return a list of all addresses stored in the database.
     */
    @Override
    public List<Address> getAllAddresses() {
        CriteriaQuery<Address> c = em.getCriteriaBuilder().createQuery(Address.class);
        c.from(Address.class);
        return em.createQuery(c).getResultList();
    }
}
