package com.dm.teamquery.test;


import com.dm.teamquery.data.service.MovieService;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.entity.Movie;
import com.dm.teamquery.execption.customexception.SearchFailedException;
import com.dm.teamquery.search.FullTextSearch;
import com.dm.teamquery.search.SearchParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TestLuceneSearchMovie {

    @Inject
    FullTextSearch<Movie> fullTextSearch;

    @Inject
    MovieService movieService;

    @PostConstruct
    private void setType() {
        fullTextSearch.setEntityType(Movie.class);
    }

    @Test
    void testNormalQueries() throws Exception {

        List<Movie> ml = movieService.findAll();

        List<Movie> ml2 = fullTextSearch.search("");

        System.out.println();

    }

    @Test
    void testFilter() throws Exception {
        assertEquals(fullTextSearch.count(""), 17);
        assertEquals(fullTextSearch.count(new SearchParameters.Builder().enabledOnly().build()),16);
        assertEquals(fullTextSearch.count("phonybalogna@yourdomain.com"), 1);
        assertEquals(fullTextSearch.count(
                new SearchParameters.Builder().withQuery("phonybalogna@yourdomain.com").enabledOnly().build()), 0);
    }

    @Test
    void testResultCount() throws Exception {
        int count = fullTextSearch.count(new SearchParameters.Builder().withPageSize(1).build());
        assertEquals(count, 17);
    }


}


