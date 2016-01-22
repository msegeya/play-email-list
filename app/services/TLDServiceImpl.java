package services;

import models.TLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Service
public class TLDServiceImpl implements TLDService {

    final Logger log = LoggerFactory.getLogger(TLDServiceImpl.class);
    @PersistenceContext
    EntityManager em;

    @Override
    public List<TLD> getAllTLDs() {
        log.debug("getting all the TLDs");

        CriteriaQuery<TLD> c = em.getCriteriaBuilder().createQuery(TLD.class);
        c.from(TLD.class);
        return em.createQuery(c).getResultList();
    }
}
