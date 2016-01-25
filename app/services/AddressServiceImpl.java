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
     * Attempt to insert an address into the database.
     */
    @Transactional
    public boolean addAddress(Address address) {
        if (checkUnique(address)) {
            em.persist(address);
            return true;
        }
        return false;
    }

    private boolean checkUnique(Address address) {
        return (em.find(Address.class, address.getAddress()) == null);
    }

    /**
     * Return a list of all addresses stored in the database.
     */
    public List<Address> getAllAddresses() {
        CriteriaQuery<Address> c = em.getCriteriaBuilder().createQuery(Address.class);
        c.from(Address.class);
        return em.createQuery(c).getResultList();
    }
}
