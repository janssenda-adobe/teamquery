package com.dm.teamquery.data.repository;


import com.dm.teamquery.entity.Auditable;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.EntityLookupException;
import com.dm.teamquery.execption.EntityNotFoundException;
import com.dm.teamquery.execption.InvalidEntityIdException;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GenericService<T extends Auditable, ID extends Serializable> {


    // private final static Logger logger = LogManager.getLogger("ServiceLog");

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ABCRepository<Challenge, UUID> ABCRepository;

//    @Inject
//    private SearchInfoRepository searchInfoRepository;

    @Setter
    private Class persistentClass;

    //    @PersistenceContext
//    EntityManagerFactory em;
//
//



    public List<Challenge> findAllEntities() {

            Iterable<Challenge> results = ABCRepository.findAll();
            List<Challenge> list = new ArrayList<>();
            results.forEach(list::add);
            return list;
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<T> cq = cb.createQuery(persistentClass);
//        Root<T> rootEntry = cq.from(persistentClass);
//        CriteriaQuery<T> all = cq.select(rootEntry);
//        TypedQuery<T> allQuery = em.createQuery(all);
//        return allQuery.getResultList();
     //   return em.createQuery("Select t from " + persistentClass.getSimpleName() + " t").getResultList();
    }

    @Transactional
    public <S extends T> S saveEntity(T t)
            throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException {
       // this.persistentClass = t.getClass();
        // ID id = getEntityId(t);
        T entity = em.merge(t);
      //  em.getTransaction().commit();
        //em.find(t, id);

        Object x = em.find(t.getClass(), getEntityId(t));

        return null;

//        repository.saveEntity(t);
//        return repository.findEntityById(((Challenge) t).getChallengeId());
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
