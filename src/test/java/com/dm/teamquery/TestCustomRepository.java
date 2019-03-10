package com.dm.teamquery;


import com.dm.teamquery.data.generic.GenS;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.EntityNotFoundException;
import com.dm.teamquery.execption.InvalidEntityIdException;
import com.dm.teamquery.execption.TeamQueryException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestCustomRepository {

    @Inject
    private GenS<Challenge, UUID> gd;

    @PostConstruct
    public void set() {
        this.gd.setPersistentClass(Challenge.class);
    }


    @FunctionalInterface
    interface ExceptionCheck<T> {void execute(T t) throws TeamQueryException; }


    private void MatchException(ExceptionCheck ec, Object o, Class c) {
        try {
            ec.execute(o);
        } catch (Exception e) {
            Throwable t = ExceptionUtils.getRootCause(e);
            Assertions.assertEquals((null == t ? e : t).getClass(), c);
        }
    }

    @Test
    public void TestFind(){
        Assertions.assertEquals(17, gd.findAll().size());
        Assertions.assertThrows(EntityNotFoundException.class, () -> gd.findById(UUID.randomUUID()));
        Assertions.assertThrows(InvalidEntityIdException.class, () -> gd.findById(null));
    }
    @Test
    public void TestAddSimple() {
        Challenge c = new Challenge();
        c.setAuthor("Carag");
        c.setAnswer("What is the question");
        c.setQuestion("What is the answer");
        c = gd.save(c);
        Assertions.assertNotNull(c.getEntityId());
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
//
//    @Test
//    public void TestUpdate() throws Exception {
//        Challenge c = gd.findAll().get(0);
//        c.setQuestion("A new one");
        //gd.save(c);
       // assertEquals(c, gd.findById(c.getEntityId()));

 //   }
//
//    @Test
//    public void TestImmutableUpdate() throws Exception {
//
//        gd.setPersistentClass(Challenge.class);
//        List<Challenge> qw = gd.findAll();
//        Challenge c = gd.findAll().get(0);
//        String original = c.getAuthor();
//
//        c.setAuthor("A new author");
//        c = gd.save(c);
//        Challenge b = gd.findById(c.getEntityId());
//        Assert.assertEquals(b.getAuthor(), original);
//
//        LocalDateTime gseatedOriginal = b.getCreatedDate();
//        b.setCreatedDate(LocalDateTime.MIN);
//        gd.save(b);
//        b = gd.findById(c.getEntityId());
//        Assert.assertEquals(b.getCreatedDate(), gseatedOriginal);
//
//        UUID oldID = b.getEntityId();
//        int cursize = gd.findAll().size();
//        b.setEntityId(UUID.randomUUID());
//        b.setQuestion("Different");
//        gd.save(b);
//
//      //  assertTrue(gd.existsEntity(oldID));
//        Assert.assertEquals(cursize + 1, gd.findAll().size());
//    }
//
//    @Test
//    public void TestAudit() throws Exception {
//
//        Challenge c = new Challenge();
//        c.setAuthor("Carag");
//        c.setAnswer("What is the question");
//        c.setQuestion("What is the answer");
//        c = gd.save(c);
//
//        c.setQuestion("An update!");
//        Thread.sleep(500);
//        c = gd.save(c);
//
//        Challenge cur = gd.findById(c.getEntityId());
//        assertNotEquals(cur.getLastModifiedDate(), cur.getCreatedDate());
//    }
//
//    @Test
//
//    public void TestDelete() throws Exception {
//
//        UUID id = gd.findAll().get(5).getEntityId();
//        gd.deleteById(id);
//        assertFalse(gd.existsEntity(id));
//
//        ExceptionCheck<UUID> ec = (e) -> gd.deleteById(e);
//
//        MatchException(ec, id, EntityNotFoundException.class);
//        MatchException(ec, UUID.randomUUID(), EntityNotFoundException.class);
//     //   MatchException(ec, null, InvalidEntityIdException.class);
//
//    }


}
