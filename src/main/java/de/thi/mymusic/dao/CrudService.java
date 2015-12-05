package de.thi.mymusic.dao;

import de.thi.mymusic.domain.BaseEntity;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Michael on 16.11.2015.
 *
 * Generic CRUD Service class (Data Access Object)
 *
 */

@Stateless
public class CrudService {

    @PersistenceContext(unitName="mymusic")
    private EntityManager em;

    private static final Logger logger = Logger.getLogger(CrudService.class);

    // Create new entity
    public <T extends BaseEntity> void persist(T entity) {
        em.persist(entity);
    }

    // Update existing entity
    public <T extends BaseEntity> T merge(T entity) {
        return em.merge(entity);
    }

    public <T extends BaseEntity> T findById(Class<T> clazz, long id) {
        return em.find(clazz, id);
    }

    public <T extends BaseEntity> List<T> findAll(Class<T> clazz) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        return em.createQuery(query.select(query.from(clazz))).getResultList();
    }

    // NamedQuery
    public <T extends BaseEntity> List<T> findByNamedQuery(Class<T> clazz, String queryName,
                                                           String[] paramNames, Object[] values) {
        TypedQuery<T> query = em.createNamedQuery(queryName, clazz);

        if(paramNames != null) {
            for(int i = 0; i < paramNames.length; i++) {
                query.setParameter(paramNames[i], values[i]);
            }
        }

        List<T> result = query.getResultList();
        logger.info("FindByNamedQuery: " + clazz.toString() + " Value:" + values[0] + ", Founded:" + result.size());
        return (List<T>) result;
    }

    public <T extends BaseEntity> void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

}
