//package com.dm.teamquery.data.generic;
//
//import com.dm.teamquery.data.repository.SearchInfoRepository;
//import com.dm.teamquery.entity.SearchInfo;
//import com.dm.teamquery.execption.DeleteFailedException;
//import com.dm.teamquery.execption.EntityLookupException;
//import com.dm.teamquery.execption.EntityNotFoundException;
//import com.dm.teamquery.execption.InvalidEntityIdException;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.support.JpaEntityInformation;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.io.Serializable;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//
//public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomRepository<T, ID> {
//
//    private final EntityManager entityManager;
//
//    public CustomRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
//        super(entityInformation, entityManager);
//        this.entityManager = entityManager;
//    }
//
//    @Override
//    public List<T> findAll() {return findAll(); }
//
//    @Override
//    @Transactional
//    public <S extends T> S saveEntity(S entity) {
//        if (existsEntity(entity)) entityManager.refresh(super.saveAndFlush(entity));
//        else super.saveAndFlush(entity);
//        return entity;
//    }
//
//    @Override
//    public T saveForReal(T entity) throws Exception{
//        saveEntity(entity);
//        return findEntityById((ID) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity));
//    }
//
//    @Override
//    public List<T> findAllEntities() {
//        return null;
//    }
//
//    @Override
//    public T findEntityById(ID id) throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException {
//        try {
//            return findById(id).get();
//        } catch (NoSuchElementException e) {
//            throw new EntityNotFoundException(e.getStackTrace(), "No entity was found for id: " + id.toString());
//        } catch (IllegalArgumentException e) {
//            throw new InvalidEntityIdException(e);
//        } catch (Exception e) {
//            throw new EntityLookupException(e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void deleteEntityById(ID id) throws EntityNotFoundException, DeleteFailedException {
//        try {
//            deleteById(id);
//        } catch (EmptyResultDataAccessException | IllegalArgumentException e) {
//            throw new EntityNotFoundException(e, "No entity was found for id: " + id);
//        } catch (Exception e) {
//            throw new DeleteFailedException(e);
//        }
//    }
//
//    @Override
//    public boolean existsEntity(ID id) {
//        return findById(id).isPresent();
//    }
//
//    @Override
//    public boolean existsEntity(T entity) {
//        ID id = (ID) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
//        return (id != null) && existsEntity(id);
//    }
//
//    @Override
//    public List<T> search(String query, Pageable p) {
//        return entityManager
//                .createQuery(query)
//                .setMaxResults(p.getPageSize())
//                .setFirstResult(p.getPageNumber() * p.getPageSize())
//                .getResultList();
//    }
//
//    @Override
//    public long count (String query) {
//        return (long) entityManager.createQuery("select count(*) " + query).getResultList().get(0);
//    }
//
//}