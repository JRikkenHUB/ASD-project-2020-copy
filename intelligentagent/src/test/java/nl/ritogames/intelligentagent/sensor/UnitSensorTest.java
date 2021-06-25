package nl.ritogames.intelligentagent.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.exception.AbsentEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnitSensorTest {

  private UnitSensor sut;

  @BeforeEach
  void setUp() {
    sut = new UnitSensor("unit");
  }

  @Test
  void emptyTileShouldReturnNull() {
    AccessibleWorldTile tile = new AccessibleWorldTile(0, 0);

    Individual result = sut.scan(tile);

    assertNull(result);
  }

  @Test
  void inaccessibleTileShouldReturnNull() {
    InaccessibleWorldTile tile = new InaccessibleWorldTile(0, 0);

    Individual result = sut.scan(tile);

    assertNull(result);
  }

  @Test
  void tileWithCharacterShouldReturnCharacter() throws AbsentEntityException {
    AccessibleWorldTile tile = new AccessibleWorldTile(0, 0);
    Character character = new Character(new ASDVector(0, 0));
    tile.addIndividual(character);

    Individual result = sut.scan(tile);

    assertEquals(character, result);
  }

    @Test
    void tileWithMonsterShouldReturnMonster() throws AbsentEntityException {
        AccessibleWorldTile tile = new AccessibleWorldTile(0, 0);
        Monster monster = new Monster(0, 0, 100, 100, 100, "name", 'M');
        tile.addIndividual(monster);

    Individual result = sut.scan(tile);

    assertEquals(monster, result);
  }

  @Test
  void sensorShouldSetOutput() throws AbsentEntityException {
    AccessibleWorldTile tile = mock(AccessibleWorldTile.class);
    Character character = mock(Character.class);
    when(tile.getIndividual()).thenReturn(character);

    sut.scan(tile);

    assertEquals(character, sut.getOutput());
  }
}
