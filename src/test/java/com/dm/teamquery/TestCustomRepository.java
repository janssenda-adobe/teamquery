package com.dm.teamquery;


import com.dm.teamquery.data.repository.GenericDaoImpl;
import com.dm.teamquery.entity.Challenge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestCustomRepository {


    @Inject
    private GenericDaoImpl<Challenge, UUID> gs;

    @PostConstruct
    public void set() {
        this.gs.setPersistentClass(Challenge.class);
    }

    @Test
    public void nothing(){
        List<Challenge> l = gs.findAll();
        System.out.println();
    }

    @Test
    public void TestUpdate() throws Exception {

        Challenge c = gs.findAll().get(0);
        c.setAuthor("A new one");
   //     Challenge b = gs1.saveForReal(c);
       // b = gs.findById(c.getEntityId());
    //    Assert.assertEquals(b, c);

    }
//
//    @Test
//    public void TestFind() {
//        Assertions.assertThrows(EntityNotFoundException.class, () -> gs.findEntityById(UUID.randomUUID()));
//        Assertions.assertThrows(InvalidEntityIdException.class, () -> gs.findEntityById(null));
//    }
//
//    @Test
//    public void TestImmutableUpdate() throws Exception {
//
//        gs.setPersistentClass(Challenge.class);
//        List<Challenge> qw = gs.findAll();
//        Challenge c = gs.findAll().get(0);
//        String original = c.getAuthor();
//
//        c.setAuthor("A new author");
//        c = gs.saveEntity(c);
//        Challenge b = gs.findEntityById(c.getChallengeId());
//        Assert.assertEquals(b.getAuthor(), original);
//
//        LocalDateTime gseatedOriginal = b.getCreatedDate();
//        b.setCreatedDate(LocalDateTime.MIN);
//        gs.saveEntity(b);
//        b = gs.findEntityById(c.getChallengeId());
//        Assert.assertEquals(b.getCreatedDate(), gseatedOriginal);
//
//        UUID oldID = b.getChallengeId();
//        int cursize = gs.findAll().size();
//        b.setChallengeId(UUID.randomUUID());
//        b.setQuestion("Different");
//        gs.saveEntity(b);
//
//        assertTrue(gs.existsEntity(oldID));
//        Assert.assertEquals(cursize + 1, gs.findAll().size());
//    }
//
//    @Test
//    public void TestBadData() {
//
//        Challenge c = gs.findAll().get(3);
//
//        c.setQuestion("");
//        Assertions.assertThrows(ConstraintViolationException.class, () -> gs.saveEntity(c));
//
//        c.setQuestion(null);
//        Assertions.assertThrows(ConstraintViolationException.class, () -> gs.saveEntity(c));
//
//        try {
//            gs.save(new Challenge());
//        } catch (Exception e) {
//            Throwable a = ExceptionUtils.getRootCause(e);
//            assertTrue(a instanceof ConstraintViolationException);
//            assertEquals(3, ((ConstraintViolationException) a).getConstraintViolations().size());
//        }
//    }
//
//
//    @Test
//    public void TestAudit() throws Exception {
//
//        Challenge c = new Challenge();
//        c.setAuthor("Carag");
//        c.setAnswer("What is the question");
//        c.setQuestion("What is the answer");
//        c = gs.saveEntity(c);
//
//        c.setQuestion("An update!");
//        Thread.sleep(500);
//        c = gs.saveEntity(c);
//
//        Challenge cur = gs.findEntityById(c.getChallengeId());
//        assertNotEquals(cur.getLastModifiedDate(), cur.getCreatedDate());
//    }
//
//    @Test
//    public void TestAddSimple() throws Exception {
//        gs.setPersistentClass(Challenge.class);
//        Challenge c = new Challenge();
//        c.setAuthor("Carag");
//        c.setAnswer("What is the question");
//        c.setQuestion("What is the answer");
//        c = gs.saveEntity(c);
//        Assertions.assertNotNull(c.getChallengeId());
//        Assertions.assertNotNull(c.getLastAuthor());
//        Assertions.assertNotNull(c.getEnabled());
//        Assertions.assertNotNull(c.getCreatedDate());
//        Assertions.assertNotNull(c.getLastModifiedDate());
//    }
//
//    @Test
//    public void TestDelete() throws Exception {
//
//        UUID id = gs.findAll().get(5).getChallengeId();
//        gs.deleteEntityById(id);
//        assertFalse(gs.existsEntity(id));
//
//        Assertions.assertThrows(EntityNotFoundException.class, () -> gs.deleteEntityById(id));
//        Assertions.assertThrows(EntityNotFoundException.class, () -> gs.deleteEntityById(UUID.randomUUID()));
//        Assertions.assertThrows(EntityNotFoundException.class, () -> gs.deleteEntityById(null));
//    }

}
