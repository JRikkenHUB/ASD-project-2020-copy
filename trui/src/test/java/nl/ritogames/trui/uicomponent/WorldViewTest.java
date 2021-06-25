package nl.ritogames.trui.uicomponent;

import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.gameobject.attribute.Armour;
import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.attribute.Potion;
import nl.ritogames.shared.dto.gameobject.attribute.Weapon;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.dto.gameobject.trap.Trap;
import nl.ritogames.shared.dto.gameobject.world.tile.*;
import nl.ritogames.trui.Fixtures;
import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.uicomponent.worldview.WorldView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorldViewTest {

  private WorldView sut;
  private ASDTile mockEdgeTile;
  private ASDTile mockAccessibleTile;
  private ASDTile mockBlockedTile;
  private TrapTile mockTrapTile;
  private UIContext mockUIContext;
  private GameStateContext mockGameStateContext;
  private AccessibleWorldTile mockAccessibleTileWithWeapon;
  private AccessibleWorldTile mockAccessibleTileWithPotion;
  private AccessibleWorldTile mockAccessibleTileWithArmor;
  private AccessibleWorldTile mockAccessibleTileWithPlayer;
  private AccessibleWorldTile mockAccessibleTileWithMonster;

  @BeforeEach
  void setup() {
    Vector size = new Vector(13, 7);
    sut = new WorldView(Vector.ZERO, size);
    mockEdgeTile = mock(EdgeTile.class);
    mockAccessibleTile = mock(AccessibleWorldTile.class);
    mockAccessibleTileWithPotion = mock(AccessibleWorldTile.class);
    mockAccessibleTileWithWeapon = mock(AccessibleWorldTile.class);
    mockAccessibleTileWithArmor = mock(AccessibleWorldTile.class);
    mockAccessibleTileWithPlayer = mock(AccessibleWorldTile.class);
    mockAccessibleTileWithMonster = mock(AccessibleWorldTile.class);
    mockTrapTile = mock(TrapTile.class);
    mockBlockedTile = mock(InaccessibleWorldTile.class);
    mockGameStateContext = mock(GameStateContext.class);
    mockUIContext = mock(UIContext.class);
  }

  @Test
  void TestIfTheMapDrawsWithoutAMapSet() {
    ASDTile[][] map = {};
    when(mockUIContext.getGameStateContext()).thenReturn(mockGameStateContext);
    when(mockGameStateContext.getWorldContext()).thenReturn(map);
    String result = sut.draw(mockUIContext).getText();

    Assertions.assertEquals(Fixtures.getWorldViewWithEmptyMap(), result);
  }

  @Test
  void TestIfTheMapGetsDrawnWithCorrectTiles() {
    ASDTile[][] map = {
        {mockEdgeTile, mockAccessibleTileWithMonster, mockAccessibleTileWithPotion},
        {mockAccessibleTileWithWeapon, mockAccessibleTile, mockAccessibleTileWithArmor},
        {mockBlockedTile, mockTrapTile, mockAccessibleTileWithPlayer}};
    when(mockUIContext.getGameStateContext()).thenReturn(mockGameStateContext);
    when(mockGameStateContext.getWorldContext()).thenReturn(map);
    when(mockTrapTile.getTrap()).thenReturn(new Trap());
    setupAttributes(mockAccessibleTileWithPotion, Potion.class);
    setupAttributes(mockAccessibleTileWithWeapon, Weapon.class);
    setupAttributes(mockAccessibleTileWithArmor, Armour.class);

    setupIndividuals(mockAccessibleTileWithPlayer, Character.class);
    setupIndividuals(mockAccessibleTileWithMonster, Monster.class);
    when(((Monster)mockAccessibleTileWithMonster.getIndividual()).getTexture()).thenReturn('M');

    String result = sut.draw(mockUIContext).getText();

    Assertions.assertEquals(Fixtures.getWorldViewWithAllTileTypes(), result);
  }

  private void setupAttributes(AccessibleWorldTile tile, Class<? extends Attribute> mockedClass) {
    when(tile.hasAttribute()).thenReturn(true);
    when(tile.getAttribute()).thenReturn(mock(mockedClass));
  }

  private void setupIndividuals(AccessibleWorldTile tile,
      Class<? extends Individual> mockedIndividual) {
    when(tile.hasIndividual()).thenReturn(true);
    when(tile.getIndividual()).thenReturn(mock(mockedIndividual));
  }

  @Test
  void TestIfTheMapDrawsAllTilesCorrectly() {
    ASDTile[][] map = {
        {mockEdgeTile, mockEdgeTile, mockEdgeTile},
        {mockAccessibleTile, mockAccessibleTile, mockAccessibleTile},
        {mockEdgeTile, mockEdgeTile, mockEdgeTile}};
    when(mockUIContext.getGameStateContext()).thenReturn(mockGameStateContext);
    when(mockGameStateContext.getWorldContext()).thenReturn(map);

    String result = sut.draw(mockUIContext).getText();

    Assertions.assertEquals(Fixtures.getWorldViewWithCorrectTilePlacement(), result);
  }
}
