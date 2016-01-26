package services;

import models.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * Attempt to insert an {@link Address} into the database.
     */
    @Transactional
    public boolean addAddress(Address address) {
        if (checkUnique(address)) {
            em.persist(address);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteAddress(Address address){
        // need to merge with entity in the DB.
        address = em.merge(address);
        em.remove(address);
    }

    /**
     * Helper method to ensure that an insertion to the database doesn't exist already in the database.
     *
     * @param address {@link Address} object of the address to be checked.
     * @return {@link Boolean}, True if the object was NOT in the database, False if it WAS.
     */
    private boolean checkUnique(Address address) {
        return (em.find(Address.class, address.getAddress()) == null);
    }

    /**
     * Return a {@link List<Address>} of all {@link Address} objects stored in the database.
     */
    public List<Address> getAllAddresses() {
        CriteriaQuery<Address> c = em.getCriteriaBuilder().createQuery(Address.class);
        c.from(Address.class);
        return em.createQuery(c).getResultList();
    }
}
