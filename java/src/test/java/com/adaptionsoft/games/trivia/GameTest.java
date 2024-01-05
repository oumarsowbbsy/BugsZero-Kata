package com.adaptionsoft.games.trivia;



import com.adaptionsoft.games.trivia.runner.GameRunner;
import com.adaptionsoft.games.uglytrivia.Game;
import org.approvaltests.Approvals;
import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class GameTest {

    private Game game;
    @Before
    public void setUp() {
        game = new Game();
    }
    @Test
	public void itsLockedDown() throws Exception {

        Random randomizer = new Random(123455);
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(resultStream));

        IntStream.range(1,15).forEach(i -> GameRunner.playGame(randomizer));

        Approvals.verify(resultStream.toString());

	}

    @Test
    public void testAddValidPlayer() {
        String playerName = "Alice";
        assertTrue("The player should be added successfully", game.add(playerName));
    }

    @Test
    public void testAddInvalidPlayer() {
        String playerName = " ";
        assertFalse("The player should not be added with an invalid name", game.add(playerName));
    }

    @Test
    public void testCreateRockQuestion() {
        int testIndex = 5;
        String expectedQuestion = "Rock Question " + testIndex;
        String actualQuestion = game.createRockQuestion(testIndex);

        assertEquals("The rock question should match the expected format", expectedQuestion, actualQuestion);
    }
}
