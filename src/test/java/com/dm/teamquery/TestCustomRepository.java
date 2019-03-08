package com.dm.teamquery;


import com.dm.teamquery.data.generic.GenericDaoImpl;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.EntityNotFoundException;
import com.dm.teamquery.execption.InvalidEntityIdException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestCustomRepository {

    @Inject
    private GenericDaoImpl<Challenge, UUID> gd;

    @PostConstruct
    public void set() {
        this.gd.setPersistentClass(Challenge.class);
    }

    @Test
    public void TestFind() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> gd.findById(UUID.randomUUID()));
        Assertions.assertThrows(InvalidEntityIdException.class, () -> gd.findById(null));
    }

    @Test
    public void TestAddSimple() throws Exception {
        gd.setPersistentClass(Challenge.class);
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

    @FunctionalInterface
    interface TestInt<T> {boolean checkBool(T t); }

    @Test
    public void TestBadData() {

        TestInt<Challenge> t = (x) -> {
            try {
                gd.save(x);} catch (Exception e)
            {return ExceptionUtils.getRootCause(e) instanceof ConstraintViolationException;} return false;};

        Challenge c = gd.findAll().get(3);
        c.setQuestion("");
        assertTrue(t.checkBool(c));
        c.setQuestion(null);
        assertTrue(t.checkBool(c));
        assertTrue(t.checkBool(new Challenge()));
    }

    @Test
    public void TestUpdate() throws Exception {
        Challenge c = gd.findAll().get(0);
        c.setQuestion("A new one");
        gd.save(c);
        assertEquals(c, gd.findById(c.getEntityId()));

    }

    @Test
    public void TestImmutableUpdate() throws Exception {

        gd.setPersistentClass(Challenge.class);
        List<Challenge> qw = gd.findAll();
        Challenge c = gd.findAll().get(0);
        String original = c.getAuthor();

        c.setAuthor("A new author");
        c = gd.save(c);
        Challenge b = gd.findById(c.getEntityId());
        Assert.assertEquals(b.getAuthor(), original);

        LocalDateTime gseatedOriginal = b.getCreatedDate();
        b.setCreatedDate(LocalDateTime.MIN);
        gd.save(b);
        b = gd.findById(c.getEntityId());
        Assert.assertEquals(b.getCreatedDate(), gseatedOriginal);

        UUID oldID = b.getEntityId();
        int cursize = gd.findAll().size();
        b.setEntityId(UUID.randomUUID());
        b.setQuestion("Different");
        gd.save(b);

      //  assertTrue(gd.existsEntity(oldID));
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

        Challenge cur = gd.findById(c.getEntityId());
        assertNotEquals(cur.getLastModifiedDate(), cur.getCreatedDate());
    }

//    @Test
//    public void TestDelete() throws Exception {
//
//        UUID id = gd.findAll().get(5).getEntityId();
//        gd.deleteEntityById(id);
//        assertFalse(gd.existsEntity(id));
//
//        Assertions.assertThrows(EntityNotFoundException.class, () -> gd.deleteEntityById(id));
//        Assertions.assertThrows(EntityNotFoundException.class, () -> gd.deleteEntityById(UUID.randomUUID()));
//        Assertions.assertThrows(EntityNotFoundException.class, () -> gd.deleteEntityById(null));
//    }

}
