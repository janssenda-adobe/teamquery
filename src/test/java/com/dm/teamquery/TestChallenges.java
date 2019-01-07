package com.dm.teamquery;


import com.dm.teamquery.config.DataGenerator;
import com.dm.teamquery.data.ChallengeRepository;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.model.Challenge;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class TestChallenges {

    @Inject ChallengeService challengeService;
    @Inject ChallengeRepository challengeRepository;
    @Inject DataGenerator dataGenerator;
    private List<Challenge> origninalList;

    @PostConstruct
    void setInitialData(){
        dataGenerator.generateChallenges();
        this.origninalList = challengeService.findAll();
    }

    @Test
    public void TestGet() {

        Challenge c = origninalList.get(15);
        Challenge d = challengeRepository.findChallengeByChallengeId(c.getChallengeId());
        Challenge e = challengeService.findChallengeByChallengeId(c.getChallengeId().toString());
        assertEquals(c, d);
        assertEquals(d, e);

    }

    @Test
    public void TestDelete() {

        Challenge c = origninalList.get(15);
        challengeService.deleteChallengeById(c.getChallengeId().toString());
        assertNull(challengeService.findChallengeByChallengeId(c.getChallengeId().toString()));

    }

    @Test
    public void TestUpdate() {

        Challenge c = origninalList.get(35);
        c.setAnswer("There is no answer...");
        Challenge d = challengeService.updateChallenge(c);
        assertEquals(d, challengeService.findChallengeByChallengeId(c.getChallengeId().toString()));

        Challenge e = new Challenge();
        e.setAnswer("This is the final answer!");
        e.setQuestion("Was there a question");
        e.setAuthor("Vossen");
        e.setDateLastModified(LocalDateTime.now());
        e.setDateCreated(LocalDateTime.now());

        e = challengeService.updateChallenge(e);
        assertEquals(e, challengeService.findChallengeByChallengeId(e.getChallengeId().toString()));

    }


}
