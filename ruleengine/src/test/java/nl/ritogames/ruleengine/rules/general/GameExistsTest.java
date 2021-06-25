package nl.ritogames.ruleengine.rules.general;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameExistsTest {
    private GameExists<GameJoinEvent> sut;
    private GameStateModifier mockedModifier;
    private GameState mockedGameState;

    @BeforeEach
    void setUp() {
        sut = new GameExists<>();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedGameState = mock(GameState.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);

        HashMap<String, Individual> individuals = new HashMap<>();
        Individual character = new Character(0, 0);
        individuals.put(INDIVIDUAL_ID, character);
        when(mockedGameState.getIndividuals()).thenReturn(individuals);
        when(mockedGameState.getName()).thenReturn(GAME);
    }


    @Test
    void testPassesReturnsTrue() {
        // Setup
        GameJoinEvent event = new GameJoinEvent(INDIVIDUAL_ID,GAME, "");
        when(mockedGameState.getStatus()).thenReturn(GameStatus.CREATED);
        when(mockedGameState.getName()).thenReturn(GAME);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testPassesReturnsFalse() {
        // Setup
        GameJoinEvent event = new GameJoinEvent(INDIVIDUAL_ID, "doesntexist", "");

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}