package nl.ritogames.ruleengine.rules.startgame;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.StartGameEvent;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StartGameEventMinimumPlayercountTest {

    private StartGameEventMinimumPlayercount sut;
    private GameStateModifier mockedModifier;
    private GameState mockedGameState;


    @BeforeEach
    void setUp() {
        sut = new StartGameEventMinimumPlayercount();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedGameState = mock(GameState.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);
    }

    @Test
    void testValidateReturnsTrue() {
        // Setup
        HashMap<String, Individual> characters = new HashMap<>();
        Character character = new Character(0, 0);
        characters.put(INDIVIDUAL_ID, character);
        characters.put("monster", new Monster(10, 10, 100, 10, 100, "name", '1'));
        when(mockedGameState.getIndividuals()).thenReturn(characters);
        final StartGameEvent event = new StartGameEvent(INDIVIDUAL_ID, GAME, new GameState());

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testValidateReturnsFalse() {
        // Setup
        HashMap<String, Individual> characters = new HashMap<>();
        when(mockedGameState.getIndividuals()).thenReturn(characters);
        final StartGameEvent event = new StartGameEvent(INDIVIDUAL_ID, GAME, new GameState());

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}
