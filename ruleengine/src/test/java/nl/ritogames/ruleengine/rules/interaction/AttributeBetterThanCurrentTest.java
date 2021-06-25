package nl.ritogames.ruleengine.rules.interaction;

import nl.ritogames.ASDGameStateModifier;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.GameState;
import nl.ritogames.shared.dto.event.PickUpEvent;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.attribute.Armour;
import nl.ritogames.shared.dto.gameobject.attribute.Weapon;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttributeBetterThanCurrentTest {

    private AttributeBetterThanCurrent sut;
    private GameStateModifier mockedModifier;
    private GameState mockedGameState;
    private Character character;
    private World world;


    @BeforeEach
    void setUp() {
        sut = new AttributeBetterThanCurrent();
        world = mock(World.class);
        mockedModifier = mock(ASDGameStateModifier.class);
        mockedGameState = mock(GameState.class);
        character = mock(Character.class);
        when(mockedModifier.getGameState()).thenReturn(mockedGameState);
        when(mockedGameState.getWorld()).thenReturn(world);
        when(mockedGameState.getIndividual(Mockito.any())).thenReturn(character);
        ASDVector vector = new ASDVector(0,0);
        when(character.getLocation()).thenReturn(vector);
    }

    @Test
    void tileDoesntContainAttribute() {
        // Setup
        int value = Character.CHARACTER_BASE_ATTACK +2;
        when(character.getAttack()).thenReturn(value);
        final PickUpEvent event = new PickUpEvent("individualId", "gameName", Direction.NORTH);
        AccessibleWorldTile tile = new AccessibleWorldTile();
        when(world.getTile(Mockito.any())).thenReturn(tile);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void tileInaccessible() {
        // Setup
        int value = Character.CHARACTER_BASE_ATTACK +2;
        when(character.getAttack()).thenReturn(value);
        final PickUpEvent event = new PickUpEvent("individualId", "gameName", Direction.NORTH);
        InaccessibleWorldTile tile = new InaccessibleWorldTile(0,0);
        when(world.getTile(Mockito.any())).thenReturn(tile);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testWeaponHasHigherValue() {
        // Setup
        int value = Character.CHARACTER_BASE_ATTACK +2;
        when(character.getAttack()).thenReturn(value);
        final PickUpEvent event = new PickUpEvent("individualId", "gameName", Direction.NORTH);
        AccessibleWorldTile tile = new AccessibleWorldTile();
        Weapon weapon = new Weapon();
        tile.setAttribute(weapon);
        when(world.getTile(Mockito.any())).thenReturn(tile);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testWeaponHasLowerValue() {
        // Setup
        int value = Character.CHARACTER_BASE_ATTACK +10;
        when(character.getAttack()).thenReturn(value);
        final PickUpEvent event = new PickUpEvent("individualId", "gameName", Direction.NORTH);
        AccessibleWorldTile tile = new AccessibleWorldTile();
        Weapon weapon = new Weapon();
        tile.setAttribute(weapon);
        when(world.getTile(Mockito.any())).thenReturn(tile);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testArmourHasHigherValue() {
        // Setup
        int value = Character.CHARACTER_BASE_DEFENSE +2;
        when(character.getDefense()).thenReturn(value);
        final PickUpEvent event = new PickUpEvent("individualId", "gameName", Direction.NORTH);
        AccessibleWorldTile tile = new AccessibleWorldTile();
        Armour armour = new Armour();
        tile.setAttribute(armour);
        when(world.getTile(Mockito.any())).thenReturn(tile);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testArmourHasLowerValue() {
        // Setup
        int value = Character.CHARACTER_BASE_DEFENSE +10;
        when(character.getDefense()).thenReturn(value);
        final PickUpEvent event = new PickUpEvent("individualId", "gameName", Direction.NORTH);
        AccessibleWorldTile tile = new AccessibleWorldTile();
        Armour armour = new Armour();
        tile.setAttribute(armour);
        when(world.getTile(Mockito.any())).thenReturn(tile);

        // Run the test
        final boolean result = sut.validate(event, mockedModifier);

        // Verify the results
        assertFalse(result);
    }
}
