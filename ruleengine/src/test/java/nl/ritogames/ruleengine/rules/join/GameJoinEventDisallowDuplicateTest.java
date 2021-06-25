package nl.ritogames.ruleengine.rules.join;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameJoinEventDisallowDuplicateTest {
    private GameJoinEventDisallowDuplicate sut;
    private GameStateModifier mockedModifier;


    @BeforeEach
    void setUp() {
        sut = new GameJoinEventDisallowDuplicate();
        mockedModifier = mock(ASDGameStateModifier.class);
        GameState mockedGameState = mock(GameState.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);

        HashMap<String, Individual> individuals = new HashMap<>();
        Individual individual = new Character(0, 0);
        individuals.put(INDIVIDUAL_ID, individual);
        when(mockedGameState.getIndividuals()).thenReturn(individuals);
    }

    @Test
    void testPassesReturnsTrue() {
        // Setup
        GameJoinEvent event = new GameJoinEvent("unique",GAME, "");

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

//    @Test
//    void testPassesReturnsFalse() {
//        // Setup
//        GameJoinEvent event = new GameJoinEvent(INDIVIDUAL_ID, GAME, "");
//
//        // Run the test
//        final boolean result = sut.validate(event, mockedModifier);
//
//        // Verify the results
//        assertFalse(result);
//    }
}
