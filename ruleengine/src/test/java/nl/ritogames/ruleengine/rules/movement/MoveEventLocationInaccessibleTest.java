package nl.ritogames.ruleengine.rules.movement;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static nl.ritogames.ruleengine.ASDEventProcessorTest.INDIVIDUAL_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MoveEventLocationInaccessibleTest {

    private MoveEventLocationInaccessible sut;
    private GameStateModifier mockedModifier;
    private GameState mockedGameState;
    final MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);


    @BeforeEach
    void setUp() {
        sut = new MoveEventLocationInaccessible();
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedGameState = mock(GameState.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);

        Character character = new Character(0, 0);
        when(mockedGameState.getIndividual(Mockito.any())).thenReturn(character);
    }

    @Test
    void testPassesReturnsTrue() {
        // Setup
        final MoveEvent event = new MoveEvent(INDIVIDUAL_ID, Direction.SOUTH);
        WorldTile[][] tiles = {{new AccessibleWorldTile(0, 0), new AccessibleWorldTile(0, 1)}, {new AccessibleWorldTile(1, 0), new AccessibleWorldTile(0, 2)}, {new AccessibleWorldTile(0, 0), new AccessibleWorldTile(0, 1), new AccessibleWorldTile(0, 1)}};
        World world = new World(tiles);
        when(mockedGameState.getWorld()).thenReturn(world);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testPassesReturnsFalse() {
        // Setup
        WorldTile[][] tiles = {{new InaccessibleWorldTile(0, 0), new InaccessibleWorldTile(0, 1)}, {new InaccessibleWorldTile(1, 0), new InaccessibleWorldTile(0, 2)}, {new InaccessibleWorldTile(0, 0), new InaccessibleWorldTile(0, 1), new InaccessibleWorldTile(0, 1)}};
        World world = new World(tiles);
        when(mockedGameState.getWorld()).thenReturn(world);
        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testPassesThrowsOutOfBounds() {
        // Setup
        WorldTile[][] tiles = {};
        World world = new World(tiles);
        when(mockedGameState.getWorld()).thenReturn(world);
        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }

}
