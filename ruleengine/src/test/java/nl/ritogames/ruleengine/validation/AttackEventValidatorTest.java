package nl.ritogames.ruleengine.validation;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.interaction.IndividualInReach;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.AttackEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttackEventValidatorTest {
    private AttackEventValidator sut;
    private GameStateModifier mockedModifier;
    private Rule<AttackEvent> mockedRule;

    @BeforeEach
    void setUp() {
        sut = new AttackEventValidator();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedRule = mock(IndividualInReach.class);
        ArrayList<Rule<AttackEvent>> rules = new ArrayList<>();
        rules.add(mockedRule);
        sut.setRules(rules);
    }

    @Test
    void testValidateEventReturnsTrue() {
        // Setup
        final AttackEvent event = new AttackEvent();
        when(mockedRule.validate(event, mockedModifier)).thenReturn(true);
        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testValidateEventReturnsFalse() {
        // Setup
        final AttackEvent event = new AttackEvent();
        when(mockedRule.validate(event, mockedModifier)).thenReturn(false);
        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}
