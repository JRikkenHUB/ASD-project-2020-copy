package nl.ritogames.ruleengine.rules.interaction;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.PickUpEvent;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.attribute.Weapon;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttributeInReachTest {

    private AttributeInReach sut;
    private PickUpEvent event;
    private GameState mockedGameState;
    private GameStateModifier mockedModifier;
    private Character character;

    @BeforeEach
    void setUp() {
        sut = new AttributeInReach();

        mockedGameState = mock(GameState.class);
        mockedModifier = mock(ASDGameStateModifier.class);

        when(mockedModifier.getGameState()).thenReturn(mockedGameState);

        WorldTile[][] tiles = {{new AccessibleWorldTile(0, 0), new AccessibleWorldTile(0, 1)}, {new AccessibleWorldTile(1, 0), new AccessibleWorldTile(0, 2)}, {new AccessibleWorldTile(0, 0), new AccessibleWorldTile(0, 1), new AccessibleWorldTile(0, 1)}};
        World world = new World(tiles);
        when(mockedGameState.getWorld()).thenReturn(world);

        character = new Character(0, 0);
        character.setIndividualID("test");
        when(mockedGameState.getIndividual(Mockito.any())).thenReturn(character);
    }

    @Test
    void testValidateTrue() {
        Direction direction = Direction.EAST;
        event = new PickUpEvent(character.getIndividualID(), "", direction);
        ((AccessibleWorldTile) mockedGameState.getWorld().getTile(new ASDVector(1, 0)))
            .setAttribute(new Weapon("weapon", 1));


        assertTrue(sut.validate(event, mockedModifier));
    }

    @Test
    void testValidateFalse() {
        Direction direction = Direction.EAST;
        event = new PickUpEvent(character.getIndividualID(), "", direction);
        assertFalse(sut.validate(event, mockedModifier));
    }
}
