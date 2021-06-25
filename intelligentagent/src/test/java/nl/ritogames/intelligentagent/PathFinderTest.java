package nl.ritogames.intelligentagent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.ritogames.intelligentagent.sensor.ArtifactSensor;
import nl.ritogames.intelligentagent.sensor.Sensor;
import nl.ritogames.intelligentagent.sensor.UnitSensor;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.attribute.Weapon;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.exception.AbsentEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathFinderTest {

  ASDTile[][] tiles;

  @BeforeEach
  void setUp() {
    initializeMap();
  }

  /*
   The Map with the two artifacts, finds artifact 1
   ^ . . . . . . . . .
    . . 1 . . . . . . .
    . . # # . # # # . .
    . . # . . # . . . #
    . . # . # . . # # .
    . . # . . . # . . .
    . . # . # . . . . .
    . . # . # . . . . .
    2 . # . # . . . . .
    . . . . # . . . . .
  */

  @Test
  void findClosestItems() {
    List<Sensor> sensors = new ArrayList<>();
    sensors.add(new ArtifactSensor("weapon"));
    ASDVector start = new ASDVector(10, 10);
    AccessibleWorldTile itemTile1 = new AccessibleWorldTile(12, 11);
    AccessibleWorldTile itemTile2 = new AccessibleWorldTile(10, 18);
    Weapon weapon1 = new Weapon("testwapen", 10);
    Weapon weapon2 = new Weapon("testwapen1", 10);
    try {
      itemTile1.addAttribute(weapon1);
      itemTile2.addAttribute(weapon2);
    } catch (AbsentEntityException e) {
      fail();
    }
    tiles[2][1] = itemTile1;
    tiles[0][8] = itemTile2;
    PathFinder.scanMap(tiles, start, sensors);

    assertEquals(weapon1, sensors.get(0).getOutput());
  }

  /*
   The Map with one artifact and one unit, finds both of them
    ^ I . . . . . . . .
    . . . . . . . . . .
    . . # # . # # # . .
    . . # . . # . . . #
    U . # . # . . # # .
    . . # . . . # . . .
    . . # . # . . . . .
    . . # . # . . . . .
    . . # . # . . . . .
    . . . . # . . . . .
  */

  @Test
  void findItemAndUnit() {
    List<Sensor> sensors = new ArrayList<>();
    sensors.add(new ArtifactSensor("weapon"));
    sensors.add(new UnitSensor("enemy"));
    ASDVector start = new ASDVector(10, 10);
    AccessibleWorldTile itemTile = new AccessibleWorldTile(11, 10);
    AccessibleWorldTile unitTile = new AccessibleWorldTile(10, 14);
    Weapon weapon = new Weapon("testwapen", 10);
    Monster monster = new Monster(10, 14, 100, 100, 100, "testmonster", 'I');
    try {
      itemTile.addAttribute(weapon);
      unitTile.addIndividual(monster);
    } catch (AbsentEntityException e) {
      fail();
    }
    tiles[1][0] = itemTile;
    tiles[0][4] = unitTile;
    PathFinder.scanMap(tiles, start, sensors);

    assertEquals(weapon, sensors.get(0).getOutput());
    assertEquals(monster, sensors.get(1).getOutput());
  }

  /*
   The Map with one artifact and one unit, finds both of them
    ^ . . . . . . . . .
    . . . . . . . . . .
    . . # # . # # # . .
    . . # . . # . . . #
    U . # . # . . # # .
    . . # . . # # . . .
    . . # . # . . . . .
    . . # . # . . . . .
    . . # . # . . . . .
    . . . . # . . . . I
  */

  @Test
  void findOnlyReachableItem() {
    List<Sensor> sensors = new ArrayList<>();
    sensors.add(new ArtifactSensor("weapon"));
    sensors.add(new UnitSensor("enemy"));
    ASDVector start = new ASDVector(10, 10);
    AccessibleWorldTile itemTile1 = new AccessibleWorldTile(19, 19);
    AccessibleWorldTile unitTile = new AccessibleWorldTile(10, 14);
    Weapon weapon = new Weapon("testwapen", 10);
    Monster monster = new Monster(10, 14, 100, 100, 100, "testmonster", 'I');
    try {
      itemTile1.addAttribute(weapon);
      unitTile.addIndividual(monster);
    } catch (AbsentEntityException e) {
      fail();
    }
    tiles[9][9] = itemTile1;
    tiles[0][4] = unitTile;
    tiles[5][5] = new InaccessibleWorldTile(15, 15);

    PathFinder.scanMap(tiles, start, sensors);

    assertNull(sensors.get(0).getOutput());
    assertEquals(monster, sensors.get(1).getOutput());
  }

  /*
   The Map to find the shortest Path for
    ^ . . . . . . . . .
    . . . . . . . . . .
    . . # # . # # # . .
    . . # . . # . . . #
    . . # . # . . # # .
    . . # . . . # . . .
    . . # . # . . . . .
    . . # . # . . ? . .
    . . # . # . . . . .
    . . . . # . . . . .

    The Solution
    ^ 1 2 3 4 . . . . .
    . . . . 5 . . . . .
    . . # # 6 # # # . .
    . . # 8 7 # . . . #
    . . # 9 # . . # # .
    . . # 0 1 2 # . . .
    . . # . # 3 4 5 . .
    . . # . # . . 6 . .
    . . # . # . . . . .
    . . . . # . . . . .
  */

  @Test
  void findTheCorrectShortestPath() {
    ASDVector start = new ASDVector(10, 10);
    ASDVector finish = new ASDVector(17, 17);

    var actual = PathFinder.findPath(tiles, start, finish);

    List<ASDVector> expected =
        Arrays.asList(
            new ASDVector(11, 10),
            new ASDVector(11, 11),
            new ASDVector(11, 12),
            new ASDVector(11, 13),
            new ASDVector(11, 14),
            new ASDVector(12, 14),
            new ASDVector(13, 14),
            new ASDVector(13, 13),
            new ASDVector(14, 13),
            new ASDVector(15, 13),
            new ASDVector(15, 14),
            new ASDVector(15, 15),
            new ASDVector(16, 15),
            new ASDVector(17, 15),
            new ASDVector(17, 16),
            new ASDVector(17, 17));
    assertEquals(expected, actual);
  }

  /*
   The Map to where there is no Path possible
   ^ . . . . . . . . .
    . . . . . . . . . .
    . . # # . # # # . .
    . . # . . # . . . #
    . . # . # . . # # .
    . . # . . # # . . .
    . . # . # . . . . .
    . . # . # . . ? . .
    . . # . # . . . . .
    . . . . # . . . . .
  */

  @Test
  void destinationCoordinatesAreUnreachable() {
    ASDVector start = new ASDVector(10, 10);
    ASDVector finish = new ASDVector(17, 17);

    tiles[5][5] = new InaccessibleWorldTile(15, 15);

    var actual = PathFinder.findPath(tiles, start, finish);

    assertTrue(actual.isEmpty());
  }

  @Test
  void destinationCoordinatesAreOutsideTheMap() {
    ASDVector start = new ASDVector(10, 10);
    ASDVector finish = new ASDVector(0, 0);

    var actual = PathFinder.findPath(tiles, start, finish);

    assertTrue(actual.isEmpty());
  }

  @Test
  void destinationCoordinatesEqualsStartCoordinates() {
    ASDVector start = new ASDVector(10, 10);
    ASDVector finish = new ASDVector(10, 10);

    var actual = PathFinder.findPath(tiles, start, finish);

    assertTrue(actual.isEmpty());
  }

  private void initializeMap() {
    // The viewable Map is 10 tiles from the start of the map, just to be sure it's working
    tiles = new ASDTile[10][10];
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        tiles[x][y] = new AccessibleWorldTile(10 + x, 10 + y);
      }
    }
    tiles[2][2] = new InaccessibleWorldTile(12, 12);
    tiles[3][2] = new InaccessibleWorldTile(13, 12);
    tiles[4][2] = new InaccessibleWorldTile(14, 12);
    tiles[5][2] = new InaccessibleWorldTile(15, 12);
    tiles[6][2] = new InaccessibleWorldTile(16, 12);
    tiles[7][2] = new InaccessibleWorldTile(17, 12);
    tiles[8][2] = new InaccessibleWorldTile(18, 12);
    tiles[6][4] = new InaccessibleWorldTile(16, 14);
    tiles[7][4] = new InaccessibleWorldTile(17, 14);
    tiles[8][4] = new InaccessibleWorldTile(18, 14);
    tiles[9][4] = new InaccessibleWorldTile(19, 14);
    tiles[2][3] = new InaccessibleWorldTile(12, 13);
    tiles[2][5] = new InaccessibleWorldTile(12, 15);
    tiles[2][6] = new InaccessibleWorldTile(12, 16);
    tiles[2][7] = new InaccessibleWorldTile(12, 17);
    tiles[1][8] = new InaccessibleWorldTile(11, 18);
    tiles[3][9] = new InaccessibleWorldTile(13, 19);
    tiles[4][8] = new InaccessibleWorldTile(14, 18);
    tiles[4][7] = new InaccessibleWorldTile(14, 17);
    tiles[5][6] = new InaccessibleWorldTile(15, 16);
    tiles[4][4] = new InaccessibleWorldTile(14, 14);
    tiles[3][5] = new InaccessibleWorldTile(13, 15);

    AccessibleWorldTile selfTile = new AccessibleWorldTile(10, 10);
    AccessibleWorldTile artifactTileFinal = new AccessibleWorldTile(17, 17);

    try {
      selfTile.addIndividual(new Character());
      artifactTileFinal.addAttribute(new Weapon("testwapen", 10));
    } catch (AbsentEntityException e) {
      fail();
    }
    tiles[0][0] = selfTile;
    tiles[7][7] = artifactTileFinal;
  }
}
