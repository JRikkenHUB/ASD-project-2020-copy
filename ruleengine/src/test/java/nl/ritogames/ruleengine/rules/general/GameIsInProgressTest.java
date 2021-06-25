package nl.ritogames.ruleengine.rules.general;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.enums.Direction;
import nl.ritogames.shared.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameIsInProgressTest {
    private GameIsInProgress<MoveEvent> sut;
    private GameStateModifier mockedModifier;


    @BeforeEach
    void setUp() {
        sut = new GameIsInProgress<>();
        mockedModifier = mock(ASDGameStateModifier.class);
        GameState mockedGameState = mock(GameState.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);
    }

    @Test
    void testPassesReturnsTrue() {
        // Setup
        when(mockedModifier.getGameState().getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);
        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testPassesReturnsFalse() {
        // Setup
        when(mockedModifier.getGameState().getStatus()).thenReturn(GameStatus.CREATED);
        MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}