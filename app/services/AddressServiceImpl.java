package services;

import models.Address;
import configs.DataConfig;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

@Service
public class AddressServiceImpl implements AddressService {

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public void addAddress(Address address) {
        em.persist(address);
    }

    @Override
    public List<Address> getAllAddresses() {
        CriteriaQuery<Address> c = em.getCriteriaBuilder().createQuery(Address.class);
        c.from(Address.class);
        return em.createQuery(c).getResultList();
    }

}