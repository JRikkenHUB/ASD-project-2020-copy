package nl.ritogames.ruleengine.rules.join;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameJoinEventPlayerExistsTest {

    private GameJoinEventPlayerExists sut;
    private GameStateModifier mockedModifier;
    private GameJoinEvent mockedJoinEvent;
    private GameState mockedGameState;
    private String correctId;

    @BeforeEach
    void setUp() {
        sut = new GameJoinEventPlayerExists();
        mockedModifier = mock(GameStateModifier.class);
        mockedJoinEvent = mock(GameJoinEvent.class);
        mockedGameState = mock(GameState.class);
        correctId = "correctId";
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);
        when(mockedJoinEvent.getIndividualId()).thenReturn(correctId);

    }

    @Test
    void validateShouldReturnTrueWhenPlayerExists() {
        when(mockedGameState.hasIndividual(anyString())).thenReturn(true);
        boolean result = sut.validate(mockedJoinEvent, mockedModifier);
        assertTrue(result);
    }

    @Test
    void validateShouldReturnFalseWhenPlayerDoesNotExist() {
        when(mockedGameState.hasIndividual(anyString())).thenReturn(false);
        boolean result = sut.validate(mockedJoinEvent, mockedModifier);
        assertFalse(result);
    }

    @Test
    void validateShouldCallGamestateOnce() {
        sut.validate(mockedJoinEvent, mockedModifier);
        verify(mockedGameState, times(1)).hasIndividual(correctId);
    }
}
