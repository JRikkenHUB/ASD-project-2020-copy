package nl.ritogames.ruleengine.validation;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.general.PlayerIsAlive;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.StartAgentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StartAgentValidatorTest {

    private StartAgentValidator sut;
    private GameStateModifier mockedModifier;
    private Rule<StartAgentEvent> mockedRule;

    @BeforeEach
    void setUp() {
        sut = new StartAgentValidator();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedRule = mock(PlayerIsAlive.class);
        ArrayList<Rule<StartAgentEvent>> rules = new ArrayList<>();
        rules.add(mockedRule);
        sut.setRules(rules);
    }


    @Test
    void testValidateEvent() {
        // Setup
        final StartAgentEvent event = new StartAgentEvent(INDIVIDUAL_ID);
        when(mockedRule.validate(event, mockedModifier)).thenReturn(true);

        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testValidateEventReturnsFalse() {
        // Setup
        final StartAgentEvent event = new StartAgentEvent(INDIVIDUAL_ID);
        when(mockedRule.validate(event, mockedModifier)).thenReturn(false);

        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}
