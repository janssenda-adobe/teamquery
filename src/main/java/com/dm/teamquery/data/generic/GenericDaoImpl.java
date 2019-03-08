package com.dm.teamquery.data.generic;


import com.dm.teamquery.execption.*;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Service
public class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

    // private final static Logger logger = LogManager.getLogger("ServiceLog");

    @PersistenceContext
    private EntityManager em;

    @Setter
    private Class persistentClass;

    @Override
    public List<T> findAll() {
        return em.createQuery("Select t from " + persistentClass.getSimpleName() + " t").getResultList();
    }

    @Override
    @Transactional
    public T save(T t) throws InvalidEntityIdException {

        return em.merge(t);


    }

    @Override
    public T findById(ID id) throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException {

        try {
            T entity = (T) em.find(this.persistentClass, id);
            if (null == entity) throw new EntityNotFoundException();
            return entity;
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No entity was found for id: " + id.toString());
        } catch (IllegalArgumentException e) {
            throw new InvalidEntityIdException("Unable to parse ID - bad format!", e);
        } catch (Exception e) {
            throw new EntityLookupException("Unhandled error occured", e);
        }
    }

    @Override
    public void deleteById(ID id) throws EntityNotFoundException, DeleteFailedException {

    }

    @Override
    public boolean existsEntity(ID id) {
        return false;
    }

    @Override
    public boolean existsEntity(T entity) {
        return false;
    }

    @Override
    public List<T> basicSearch(String query) throws SearchFailedException {
        return null;
    }

    @Override
    public SearchResponse search(SearchRequest request) throws SearchFailedException {
        return null;
    }

    @Override
    public long count(String query) {
        return 0;
    }
//
//    public void deleteById(UUID id)
//            throws EntityNotFoundException, DeleteFailedException {
//        repository.deleteEntityById(id);
//    }
//
//    public T getById(UUID challengeId)
//            throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException {
//        return repository.findEntityById(challengeId);
//    }
//
//    public List<Challenge> basicSearch(String query) throws SearchFailedException {
//        return (List<Challenge>) search(new SearchRequest(query)).getResultsList();
//    }
//
//    public SearchResponse search(SearchRequest request) throws SearchFailedException {
//
//        long startTime = System.nanoTime();
//        String query = request.getQuery();
//        String dbQuery = prepareQuery(new Search(Challenge.class, query).getDatabaseQuery(), request.getIncDisabled());
//        SearchInfo search = new SearchInfo(query, dbQuery);
//        SearchResponse response = new SearchResponse(request);
//
//        logger.debug("Processing search request from " + request.getClient_ip() + ", query: " + (query.isEmpty() ? "[none]" : query));
//
//        try {
//            response.setRowCount(repository.count(dbQuery));
//            response.setResultsList(repository.search(dbQuery, request.getPageable()));
//            response.setSearchTime((System.nanoTime() - startTime) * 1.0e-9);
//            searchInfoRepository.save(search);
//            return response;
//        } catch (Exception e) {
//            search.setErrors(ExceptionUtils.getRootCauseMessage(e));
//            searchInfoRepository.save(search);
//            throw new SearchFailedException(ExceptionUtils.getRootCauseMessage(e));
//        }
//    }
//
//    private String prepareQuery(String query, boolean disabled) {
//        String key = disabled ? "0" : "1";
//        if (query.equals("from Challenge"))
//            return "from Challenge where enabled = " + key;
//        else {
//            return query.replace("from Challenge where ", "from Challenge where enabled = " + key + " and (") + ")";
//        }
//    }

    private UUID getEntityId(T t) {
        return (UUID) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(t);
    }

}
