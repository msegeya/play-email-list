package services;

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.TLD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;


@Service
public class TLDServiceImpl implements TLDService {

    @PersistenceContext
    EntityManager em;

    final Logger log = LoggerFactory.getLogger(TLDServiceImpl.class);

    @Override
    public List<TLD> getAllTLDs() {
        log.error("getting all the TLDs");
        return null;
//        CriteriaQuery<TLD> c = em.getCriteriaBuilder().createQuery(TLD.class);
//        c.from(TLD.class);
//        return em.createQuery(c).getResultList();
    }
}
