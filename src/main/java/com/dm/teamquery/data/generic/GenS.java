package com.dm.teamquery.data.generic;

import com.dm.teamquery.execption.EntityLookupException;
import com.dm.teamquery.execption.EntityNotFoundException;
import com.dm.teamquery.execption.InvalidEntityIdException;
import com.dm.teamquery.execption.TeamQueryException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Component
public class GenS<T, ID extends Serializable> {

    @Inject
    GenericDaoImpl<T, ID> gd;

    private Class persistentClass;

    public void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
        gd.setPersistentClass(persistentClass);
    }

    public List<T> findAll() {
        return gd.findAll();
    }

    public T findById(ID id) throws TeamQueryException {
        try {
            T entity = gd.findById(id);
            if (null == entity) throw new EntityNotFoundException();
            return entity;
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No entity was found for id: " + id.toString());
        } catch (IllegalArgumentException e) {
            throw new InvalidEntityIdException("Unable to parse ID - bad format!", e);
        } catch (Exception e) {
            throw new EntityLookupException("Unhandled exception: ", e);
        }
    }
//
//    @Transactional
//    public void deleteById(ID id) throws TeamQueryException {
//        T entity = findById(id);
//        try {
//            em.remove(entity);
//        } catch (Exception e) {
//            throw new DeleteFailedException(e);
//        }
//    }
//
//    public List<T> findAll() {
//        return em.createQuery("Select t from " + persistentClass.getSimpleName() + " t").getResultList();
//    }
//

    public T save(T t) {
        try {
            t = gd.save(t);
            return gd.find(t);
        } catch (Exception e) {
            Throwable x = ExceptionUtils.getRootCause(e);
        }
        return null;
    }
//
//    public boolean existsEntity(ID id) {
//        try {
//            findById(id);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public boolean existsEntity(T t) {
//        return existsEntity(getEntityId(t));
//    }
//
//    public ID getEntityId(T t) {
//        return (ID) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(t);
//    }
//
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
