package com.dm.teamquery.data.generic;

import lombok.Setter;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Component
public class GenericDaoImpl<T, ID extends Serializable> {

    @PersistenceContext
    private EntityManager em;


    @Setter
    private Class persistentClass;

    public List<T> findAll() {
        return em.createQuery("Select t from " + persistentClass.getSimpleName() + " t").getResultList();
    }

    public T findById(ID id) {return (T) em.find(this.persistentClass, id);}
    public T find(T t) {return findById(getEntityId(t));}

    @Transactional
    public void delete(T t) {em.remove(t);}

    @Transactional
    public T save(T t) {
        return em.merge(t);
    }

    public boolean existsEntity(ID id) {return null != findById(id);}

    public boolean existsEntity(T t) {return existsEntity(getEntityId(t));}

    public ID getEntityId(T t) {
        return (ID) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(t);
    }

//    public List<T> basicSearch(String query) throws SearchFailedException {
//        return null;
//    }
//
//    public SearchResponse search(SearchRequest request) throws SearchFailedException {
//        return null;
//    }
//
//    public long count(String query) {
//        return 0;
//    }
//
//    public void deleteById(UUID id)
//            throws EntityNotFoundException, DeleteFailedException {
//        repository.deleteEntityById(id);
//    }
//

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


}
