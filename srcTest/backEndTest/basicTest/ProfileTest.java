package backEndTest.basicTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import backEnd.basic.Profile;

public class ProfileTest {
    @Test
    void test() {
        Profile testProfile = new Profile("test12");
        testProfile.setWins(20, testProfile.getId());
        testProfile.setGames(30, testProfile.getId());
        testProfile.setPoints(8888,testProfile.getId());
        assertEquals(20,testProfile.getWins());
        assertEquals(30,testProfile.getGames());
        assertEquals(8888,testProfile.getPoints());
    }
}
