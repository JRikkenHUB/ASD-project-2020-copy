package nl.ritogames.ruleengine.rules.savegame;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.general.GameExists;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.GameSaveEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameSaveEventGameExistsTest {

    private GameExists<GameSaveEvent> sut;
    private GameStateModifier mockedModifier;

    @BeforeEach
    void setUp() {
        sut = new GameExists<>();
        mockedModifier = mock(ASDGameStateModifier.class);
        GameState mockedGameState = mock(GameState.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);
        when(mockedGameState.getName()).thenReturn(GAME);
    }


    @Test
    void testValidateReturnsTrue() {
        // Setup
        final GameSaveEvent event = new GameSaveEvent(INDIVIDUAL_ID, GAME);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testValidateReturnsFalse() {
        // Setup
        final GameSaveEvent event = new GameSaveEvent(INDIVIDUAL_ID, "gameThatDoesntExist");

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }

}
