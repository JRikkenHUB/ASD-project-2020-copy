package nl.ritogames.ruleengine.rules.general;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.host.PlayerIsHost;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.StopGameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerIsHostTest {
    private PlayerIsHost<StopGameEvent> sut;
    private GameStateModifier mockedModifier;

    @BeforeEach
    void setUp() {
        String hostIndividualId = "HOST";
        sut = new PlayerIsHost<>();
        mockedModifier = mock(ASDGameStateModifier.class);
        GameState mockedGameState = mock(GameState.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);
        when(mockedGameState.getHostIndividualId()).thenReturn(hostIndividualId);
    }

    @Test
    void testPassesReturnsTrue() {
        // Setup
        StopGameEvent event = new StopGameEvent("HOST", GAME);
        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testPassesReturnsFalse() {
        // Setup
        StopGameEvent event = new StopGameEvent(INDIVIDUAL_ID, GAME);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}
