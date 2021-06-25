package nl.ritogames.ruleengine.validation;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.interaction.AttributeInReach;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.PickUpEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PickUpEventValidatorTest {
    private PickUpEventValidator sut;
    private GameStateModifier mockedModifier;
    private Rule<PickUpEvent> mockedRule;

    @BeforeEach
    void setUp() {
        sut = new PickUpEventValidator();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedRule = mock(AttributeInReach.class);
        ArrayList<Rule<PickUpEvent>> rules = new ArrayList<>();
        rules.add(mockedRule);
        sut.setRules(rules);
    }

    @Test
    void testValidateEventReturnsTrue() {
        // Setup
        final PickUpEvent event = new PickUpEvent();
        when(mockedRule.validate(event, mockedModifier)).thenReturn(true);
        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testValidateEventReturnsFalse() {
        // Setup
        final PickUpEvent event = new PickUpEvent();
        when(mockedRule.validate(event, mockedModifier)).thenReturn(false);
        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}
