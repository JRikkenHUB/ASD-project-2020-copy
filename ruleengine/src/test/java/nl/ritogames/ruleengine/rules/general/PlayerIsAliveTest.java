package nl.ritogames.ruleengine.rules.general;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerIsAliveTest {
    private PlayerIsAlive<MoveEvent> sut;
    private GameStateModifier mockedModifier;
    GameState mockedGameState;


    @BeforeEach
    void setUp() {
        sut = new PlayerIsAlive<>();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedGameState = mock(GameState.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);

        HashMap<String, Individual> individuals = new HashMap<>();
        Individual individual = new Character(0, 0);
        individuals.put(INDIVIDUAL_ID, individual);
        when(mockedModifier.getGameState().getIndividuals()).thenReturn(individuals);
    }

    @Test
    void testPassesReturnsTrue() {
        // Setup
        MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);
        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testPassesReturnsFalse() {
        // Setup
        MoveEvent event = new MoveEvent("nonExistentID", Direction.SOUTH);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}