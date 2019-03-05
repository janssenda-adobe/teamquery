package com.dm.teamquery.data.repository;

import com.dm.teamquery.entity.Auditable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;


public interface ABCRepository<T extends Auditable, ID extends Serializable> extends PagingAndSortingRepository<T, ID>{

}



