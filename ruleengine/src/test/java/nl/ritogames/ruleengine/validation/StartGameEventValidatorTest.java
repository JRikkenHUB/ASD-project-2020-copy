package nl.ritogames.ruleengine.validation;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.general.GameExists;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.StartGameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.GAME;
import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StartGameEventValidatorTest {

    private StartGameEventValidator sut;
    private GameStateModifier mockedModifier;
    private Rule<StartGameEvent> mockedRule;

    @BeforeEach
    void setUp() {
        sut = new StartGameEventValidator();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedRule = mock(GameExists.class);
        ArrayList<Rule<StartGameEvent>> rules = new ArrayList<>();
        rules.add(mockedRule);
        sut.setRules(rules);
    }

    @Test
    void testValidateEvent() {
        // Setup
        final StartGameEvent event = new StartGameEvent(INDIVIDUAL_ID, GAME, new GameState());
        when(mockedRule.validate(event, mockedModifier)).thenReturn(true);

        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }
}
