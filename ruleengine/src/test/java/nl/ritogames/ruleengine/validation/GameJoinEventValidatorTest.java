package nl.ritogames.ruleengine.validation;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.join.GameJoinEventDisallowDuplicate;
import nl.ritogames.ruleengine.rules.join.GameJoinEventGameHasNotStarted;
import nl.ritogames.ruleengine.rules.join.GameJoinEventPlayerExists;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.GameJoinEvent;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameJoinEventValidatorTest {

    private GameJoinEventValidator sut;
    private GameStateModifier mockedModifier;
    private Rule<GameJoinEvent> mockedRuleDisallowDuplicate;
    private Rule<GameJoinEvent> mockedRuleNotStarted;
    private Rule<GameJoinEvent> mockedRulePlayerExist;
    private GameJoinEvent mockedEvent;
    private GameState mockedGameState;
    private ArrayList<Rule<GameJoinEvent>> lobbyRules;
    private ArrayList<Rule<GameJoinEvent>> gameRules;

    @BeforeEach
    void setUp() {
        sut = new GameJoinEventValidator();
        initiateMocks();
        setUpRules();
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);
        when(mockedRuleDisallowDuplicate.validate(any(), any())).thenReturn(true);
        when(mockedRuleNotStarted.validate(any(), any())).thenReturn(true);
        when(mockedRulePlayerExist.validate(any(), any())).thenReturn(true);
    }

    private void initiateMocks() {
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedRuleDisallowDuplicate = mock(GameJoinEventDisallowDuplicate.class);
        mockedRuleNotStarted = mock(GameJoinEventGameHasNotStarted.class);
        mockedRulePlayerExist = mock(GameJoinEventPlayerExists.class);
        mockedEvent = mock(GameJoinEvent.class);
        mockedGameState = mock(GameState.class);
    }

    private void setUpRules() {
        lobbyRules = new ArrayList<>();
        gameRules = new ArrayList<>();
        lobbyRules.add(mockedRuleDisallowDuplicate);
        lobbyRules.add(mockedRuleNotStarted);
        gameRules.add(mockedRulePlayerExist);
    }

    @Test
    void validateEventShouldReturnTrueWhenAllLobbyRulesReturnTrue() {
        // Setup
        sut.setRules(lobbyRules);
        when(mockedGameState.getStatus()).thenReturn(GameStatus.CREATED);

        // Run the test
        final boolean result = sut.validateEvent(mockedEvent, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void validateEventShouldReturnTrueWhenAllGameRulesReturnTrue() {
        // Setup
        sut.setRules(gameRules);
        when(mockedEvent.getIndividualId()).thenReturn("id");
        when(mockedModifier.getGameState().getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(mockedModifier.getGameState().hasIndividual("id")).thenReturn(true);

        // Run the test
        final boolean result = sut.validateEvent(mockedEvent, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void validateEventShouldReturnFalseWhenDisallowDuplicateIsFalse() {
        // Setup
        sut.setRules(lobbyRules);

        when(mockedModifier.getGameState().getStatus()).thenReturn(GameStatus.CREATED);
        HashMap<String, Individual> individualHashMap = new HashMap<>();
        individualHashMap.put("id", new Character());
        when(mockedModifier.getGameState().getIndividuals()).thenReturn(individualHashMap);
        when(mockedEvent.getIndividualId()).thenReturn("id");

        // Run the test
        final boolean result = sut.validateEvent(mockedEvent, mockedModifier);

        // Verify the results
        assertFalse(result);
    }


    @Test
    void validateEventShouldReturnFalseWhenNotStartedIsFalse() {
        // Setup
        sut.setRules(lobbyRules);
        when(mockedModifier.getGameState().getStatus()).thenReturn(GameStatus.STOPPED);
        when(mockedRuleNotStarted.validate(any(), any())).thenReturn(false);

        // Run the test
        final boolean result = sut.validateEvent(mockedEvent, mockedModifier);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void validateEventShouldReturnFalseWhenPlayerExistsIsFalse() {
        // Setup
        sut.setRules(gameRules);
        when(mockedModifier.getGameState().getStatus()).thenReturn(GameStatus.IN_PROGRESS);
        when(mockedRulePlayerExist.validate(any(), any())).thenReturn(false);

        // Run the test
        final boolean result = sut.validateEvent(mockedEvent, mockedModifier);

        // Verify the results
        assertFalse(result);
    }


}
