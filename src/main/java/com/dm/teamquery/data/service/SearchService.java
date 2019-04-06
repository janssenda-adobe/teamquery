package com.dm.teamquery.data.service;

import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.customexception.SearchFailedException;
import com.dm.teamquery.search.SLProcessor;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.stream;

@Repository
@Transactional
public class SearchService {

    private FullTextEntityManager fullTextEm;

    @Inject
    public SearchService(EntityManagerFactory emf) {
        this.fullTextEm = Search.getFullTextEntityManager(emf.createEntityManager());
    }

    public List<Challenge> search(String query, Class entityType) throws SearchFailedException {
        return search(query, entityType, PageRequest.of(0, 100));
    }

    public List<Challenge> search(String query, Class entityType, Pageable p) throws SearchFailedException {
            return parseQuery(query, entityType, p).getResultList();
    }

    public int count(String query, Class entityType) throws SearchFailedException {
        return count(query, entityType, PageRequest.of(0, 100));
    }

    public int count(String query, Class entityType, Pageable p) throws SearchFailedException {
        return parseQuery(query, entityType, p).getResultSize();
    }

    private FullTextQuery parseQuery (String query, Class entityType, Pageable p) throws SearchFailedException {

        Assert.notNull(query, "Query must not be null");
        Assert.notNull(entityType, "Entity type must not be null");

        MultiFieldQueryParser queryParser =
                new MultiFieldQueryParser(getEntityFields(entityType),
                        fullTextEm.getSearchFactory().getAnalyzer(entityType));

        queryParser.setAllowLeadingWildcard(true);

        try {
            query = new SLProcessor().format(query);
            Query r = queryParser.parse(query);
            FullTextQuery jpaQuery = fullTextEm.createFullTextQuery(r, entityType);

            return jpaQuery.setMaxResults(p.getPageSize()).setFirstResult(p.getPageNumber() * p.getPageSize());

        } catch (ParseException e) {
            throw new SearchFailedException(e.getStackTrace(),
                    e.getMessage().split("\\v")[0].replace("~", ""));
        }
    }

    private String[] getEntityFields(Class c) {
        Set<String> fieldNames = new HashSet<>();
        while (c != null) {
            stream(c.getDeclaredFields()).map(Field::getName).forEach(fieldNames::add);
            c = c.getSuperclass();
        }
        return fieldNames.toArray(new String[0]);
    }
}
