package com.dm.teamquery;


import com.dm.teamquery.data.repository.ChallengeRepository;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.TeamQueryException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestTeamQueryRepository {

    @Inject
    private ChallengeRepository gd;

    @FunctionalInterface
    interface ExceptionCheck<T> {
        void execute(T t) throws TeamQueryException;
    }

    private void MatchException(ExceptionCheck ec, Object o, Class c) {
        try {
            ec.execute(o);
        } catch (Exception e) {
            Throwable t = ExceptionUtils.getRootCause(e);
            Assertions.assertEquals((null == t ? e : t).getClass(), c);
        }
    }

    @Test
    public void TestFind() {
        ExceptionCheck<UUID> ec = (e) -> gd.findByIdThrows(e);
        Assertions.assertEquals(17, gd.findAll().size());
        MatchException(ec, UUID.randomUUID(), EntityNotFoundException.class);
        MatchException(ec, null, IllegalArgumentException.class);
    }

    @Test
    public void TestAddSimple() {
        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        c = gd.save(c);
        Assertions.assertNotNull(c.getChallengeId());
        Assertions.assertNotNull(c.getLastAuthor());
        Assertions.assertNotNull(c.getEnabled());
        Assertions.assertNotNull(c.getCreatedDate());
        Assertions.assertNotNull(c.getLastModifiedDate());
    }

    @Test
    public void TestBadData() {
        ExceptionCheck<Challenge> ec = (e) -> gd.save(e);
        Challenge c = gd.findAll().get(3);
        c.setQuestion("");
        MatchException(ec, c, ConstraintViolationException.class);
        c.setQuestion(null);
        MatchException(ec, c, ConstraintViolationException.class);
        MatchException(ec, new Challenge(), ConstraintViolationException.class);
    }

    @Test
    public void TestUpdate(){
        Challenge c = gd.findAll().get(0);
        c.setQuestion("A new one");
        gd.save(c);
        Assertions.assertEquals(c, gd.findByIdThrows(c.getChallengeId()));

    }

    @Test
    public void TestImmutableUpdate() {

        Challenge c = gd.findAll().get(0);
        String original = c.getAuthor();

        c.setAuthor("A new author");
        c = gd.save(c);
        Challenge b = gd.findByIdThrows(c.getChallengeId());
        Assert.assertEquals(b.getAuthor(), original);

        LocalDateTime gseatedOriginal = b.getCreatedDate();
        b.setCreatedDate(LocalDateTime.MIN);
        gd.save(b);
        b = gd.findByIdThrows(c.getChallengeId());
        Assert.assertEquals(b.getCreatedDate(), gseatedOriginal);

        int cursize = gd.findAll().size();
        b.setChallengeId(UUID.randomUUID());
        b.setQuestion("Different");
        b = gd.save(b);

        Assert.assertTrue(gd.existsById(b.getChallengeId()));
        Assert.assertEquals(cursize + 1, gd.findAll().size());
    }

    @Test
    public void TestAudit() throws Exception {

        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        c = gd.save(c);

        c.setQuestion("An update!");
        Thread.sleep(500);
        c = gd.save(c);

        Challenge cur = gd.findByIdThrows(c.getChallengeId());
        Assert.assertNotEquals(cur.getLastModifiedDate(), cur.getCreatedDate());
    }

    @Test
    public void TestDelete() {

        UUID id = gd.findAll().get(5).getChallengeId();
        gd.deleteById(id);
        Assert.assertFalse(gd.existsById(id));

        ExceptionCheck<UUID> ec = (e) -> gd.deleteById(e);
        MatchException(ec, id, EmptyResultDataAccessException.class);
        MatchException(ec, UUID.randomUUID(), EmptyResultDataAccessException.class);
        MatchException(ec, null, IllegalArgumentException.class);

    }


}
