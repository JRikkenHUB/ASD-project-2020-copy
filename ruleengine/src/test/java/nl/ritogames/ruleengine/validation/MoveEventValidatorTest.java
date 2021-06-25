package nl.ritogames.ruleengine.validation;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.movement.MoveEventLocationInaccessible;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MoveEventValidatorTest {

    private MoveEventValidator sut;
    private GameStateModifier mockedModifier;
    private Rule<MoveEvent> mockedRule;

    @BeforeEach
    void setUp() {
        sut = new MoveEventValidator();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedRule = mock(MoveEventLocationInaccessible.class);
        ArrayList<Rule<MoveEvent>> rules = new ArrayList<>();
        rules.add(mockedRule);
        sut.setRules(rules);
    }

    @Test
    void testValidateEventReturnsTrue() {
        // Setup
        final MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);
        when(mockedRule.validate(event, mockedModifier)).thenReturn(true);
        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testValidateEventReturnsFalse() {
        // Setup
        final MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);
        when(mockedRule.validate(event, mockedModifier)).thenReturn(false);
        // Run the test
        final boolean result = sut.validateEvent(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}
