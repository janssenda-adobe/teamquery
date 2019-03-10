package com.dm.teamquery.data.generic;

import com.dm.teamquery.execption.*;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, ID extends Serializable> {

    List<T> findAll();
    T save(T entity) throws TeamQueryException;
    T findById(ID id) throws TeamQueryException;
    void deleteById(ID id) throws TeamQueryException;

    boolean existsEntity(ID id);
    boolean existsEntity(T entity);

    public List<T> basicSearch(String query) throws SearchFailedException;
    public SearchResponse search(SearchRequest request) throws SearchFailedException;
    
    long count (String query);
}
