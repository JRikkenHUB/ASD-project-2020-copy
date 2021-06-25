package nl.ritogames.ruleengine.rules.join;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameJoinEventGameHasNotStartedTest {

    private GameJoinEventGameHasNotStarted sut;
    private GameStateModifier mockedModifier;
    private GameState mockedGameState;
    private GameJoinEvent mockedEvent;

    @BeforeEach
    void setUp() {
        sut = new GameJoinEventGameHasNotStarted();
        mockedModifier = mock(GameStateModifier.class);
        mockedGameState = mock(GameState.class);
        mockedEvent = mock(GameJoinEvent.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);
    }

    @Test
    void validateShouldReturnTrueWhenGameInLobby() {
        when(mockedGameState.getStatus()).thenReturn(GameStatus.CREATED);
        assertTrue(sut.validate(mockedEvent, mockedModifier));
    }

    @Test
    void validateShouldReturnFalseWhenGameHasStarted() {
        when(mockedGameState.getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        assertFalse(sut.validate(mockedEvent, mockedModifier));
    }

    @Test
    void validateShouldReturnFalseWhenGameHasEnded() {
        when(mockedGameState.getStatus()).thenReturn(GameStatus.FINISHED);
        assertFalse(sut.validate(mockedEvent, mockedModifier));
    }

    @Test
    void validateShouldReturnFalseWhenGameStatusIsNull() {
        when(mockedGameState.getStatus()).thenReturn(null);
        assertFalse(sut.validate(mockedEvent, mockedModifier));
    }
}
