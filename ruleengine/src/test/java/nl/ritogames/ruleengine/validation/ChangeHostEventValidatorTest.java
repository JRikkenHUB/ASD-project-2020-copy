package nl.ritogames.ruleengine.validation;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.general.PlayerIsAlive;
import nl.ritogames.ruleengine.rules.host.PlayerIsNotHost;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.ChangeHostEvent;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.mockito.Mockito.when;

public class ChangeHostEventValidatorTest {
    private final String oldHostId = "oldHost";
    private final String newHostId = "newHost";
    private ChangeHostEventValidator sut;
    private GameStateModifier mockedGameStateModifier;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        sut = new ChangeHostEventValidator();
        mockedGameStateModifier = Mockito.mock(GameStateModifier.class);

        Character oldHost = new Character();
        oldHost.setIndividualID(oldHostId);

        Character newHost = new Character();
        oldHost.setIndividualID(newHostId);

        gameState = new GameState("gameName");
        gameState.setHostIndividualId(oldHostId);
        gameState.setIndividuals(new HashMap<>() {{
            put(oldHostId, oldHost);
            put(newHostId, newHost);
        }});

        when(mockedGameStateModifier.getGameState()).thenReturn(gameState);
    }

    @Test
    void validatorAddsPlayerIsNotHostRule() {
        boolean containsPlayerIsNotHostRule = false;

        for (Rule rule : sut.getRules()) {
            if (rule instanceof PlayerIsNotHost) containsPlayerIsNotHostRule = true;
        }

        Assertions.assertTrue(containsPlayerIsNotHostRule);
    }

    @Test
    void validatorAddsPlayerIsAliveRule() {
        boolean containsPlayerIsAliveRule = false;

        for (Rule rule : sut.getRules()) {
            if (rule instanceof PlayerIsAlive) {
                containsPlayerIsAliveRule = true;
                break;
            }
        }

        Assertions.assertTrue(containsPlayerIsAliveRule);
    }

    @Test
    void validatorFailsWhenIsAliveRuleFails() {
        HashMap<String, Individual> individualMap = gameState.getIndividuals();
        individualMap.remove(newHostId);
        gameState.setIndividuals(individualMap);

        Assertions.assertFalse(sut.validateEvent(new ChangeHostEvent(newHostId, "fakeIP"), mockedGameStateModifier));
    }

    @Test
    void validatorFailsWhenIsNotHostRuleFails() {
        Assertions.assertFalse(sut.validateEvent(new ChangeHostEvent(oldHostId, "fakeIP"), mockedGameStateModifier));
    }
}
