package services;

import models.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    /**
     * Attempt to insert an address into the database. Catch an exception if the entity is already in the database.
     */
    @Override
    @Transactional
    public void addAddress(Address address) {

        try {
            log.debug("Attempting to store {}.", address.toString());
            em.persist(address);
        } catch (EntityExistsException ignored) {

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
