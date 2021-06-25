package nl.ritogames.ruleengine.rules.movement;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MoveEventOtherPlayerOnLocationTest {

    public static final String UUID_BLOCKING = "UUID";
    private MoveEventOtherPlayerOnLocation sut;
    private GameStateModifier mockedModifier;
    private GameState mockedGameState;
    final MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);

    @BeforeEach
    void setUp() {
        sut = new MoveEventOtherPlayerOnLocation();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedGameState = new GameState();
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);
        when(mockedModifier.getNextLocation(any(ASDVector.class),any(Direction.class))).thenReturn(new ASDVector(3,3));
        World world = new World();
        WorldTile[][] worldTiles = {
                {
                        new AccessibleWorldTile(0, 0),
                        new AccessibleWorldTile(0, 1),
                        new AccessibleWorldTile(0, 2),
                        new AccessibleWorldTile(0, 3),
                        new AccessibleWorldTile(0, 4),
                },
                {
                        new AccessibleWorldTile(1, 0),
                        new AccessibleWorldTile(1, 1),
                        new AccessibleWorldTile(1, 2),
                        new AccessibleWorldTile(1, 3),
                        new AccessibleWorldTile(1, 4),
                },
                {
                        new AccessibleWorldTile(2, 0),
                        new AccessibleWorldTile(2, 1),
                        new AccessibleWorldTile(2, 2),
                        new AccessibleWorldTile(2, 3),
                        new AccessibleWorldTile(2, 4),
                },
                {
                        new AccessibleWorldTile(3, 0),
                        new AccessibleWorldTile(3, 1),
                        new AccessibleWorldTile(3, 2),
                        new AccessibleWorldTile(3, 3),
                        new AccessibleWorldTile(3, 4),
                },
                {
                        new AccessibleWorldTile(4, 0),
                        new AccessibleWorldTile(4, 1),
                        new AccessibleWorldTile(4, 2),
                        new AccessibleWorldTile(4, 3),
                        new AccessibleWorldTile(4, 4),
                }
        };
        world.setTiles(worldTiles);
        mockedGameState.setIndividuals(new HashMap<>());
        ASDVector location = new ASDVector(2,2);
        Character character = new Character();
        character.setLocation(location);
        ((AccessibleWorldTile)world.getTile(location)).setIndividual(character);
        mockedGameState.setWorld(world);
        mockedGameState.addIndividual(INDIVIDUAL_ID, character);

    }

    @Test
    void validateReturnsTrue() {
        // Setup

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void validateReturnsFalseWhenCharacterIsBlocking() {
        // Setup
        Individual blockingIndividual = new Character(3, 3);
        mockedGameState.addIndividual(UUID_BLOCKING, blockingIndividual);
        ((AccessibleWorldTile)mockedGameState.getWorld().getTile(3,3)).setIndividual(blockingIndividual);
        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void validateReturnsFalseWhenMonsterIsBlocking() {
        // Setup
        Individual blockingIndividual = new Monster(3, 3, 1, 1, 1, "", 'x');
        mockedGameState.addIndividual(UUID_BLOCKING, blockingIndividual);
        ((AccessibleWorldTile)mockedGameState.getWorld().getTile(3,3)).setIndividual(blockingIndividual);
        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}
