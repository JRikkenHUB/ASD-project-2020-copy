package nl.ritogames.intelligentagent.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.ritogames.shared.dto.gameobject.attribute.Armour;
import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.attribute.Potion;
import nl.ritogames.shared.dto.gameobject.attribute.Weapon;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.exception.AbsentEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArtifactSensorTest {

  private ArtifactSensor sut;

  @BeforeEach
  void setUp() {
    sut = new ArtifactSensor("object");
  }

  @Test
  void emptyTileShouldReturnNull() {
    AccessibleWorldTile tile = new AccessibleWorldTile(0, 0);

    Attribute result = sut.scan(tile);

    assertNull(result);
  }

  @Test
  void inaccessibleTileShouldReturnNull() {
    InaccessibleWorldTile tile = new InaccessibleWorldTile(0, 0);

    Attribute result = sut.scan(tile);

    assertNull(result);
  }

    @Test
    void tileWithPotionShouldReturnPotion() throws AbsentEntityException {
        AccessibleWorldTile tile = new AccessibleWorldTile(0, 0);
        Potion potion = mock(Potion.class);
        tile.addAttribute(potion);

    Attribute result = sut.scan(tile);

    assertEquals(potion, result);
  }

  @Test
  void tileWithWeaponShouldReturnWeapon() throws AbsentEntityException {
    AccessibleWorldTile tile = new AccessibleWorldTile(0, 0);
    Weapon weapon = mock(Weapon.class);
    tile.addAttribute(weapon);

    Attribute result = sut.scan(tile);

    assertEquals(weapon, result);
  }

  @Test
  void tileWithArmorShouldReturnArmor() throws AbsentEntityException {
    AccessibleWorldTile tile = new AccessibleWorldTile(0, 0);
    Armour armor = mock(Armour.class);
    tile.addAttribute(armor);

    Attribute result = sut.scan(tile);

    assertEquals(armor, result);
  }

  @Test
  void sensorShouldSetOutput() {
    AccessibleWorldTile tile = mock(AccessibleWorldTile.class);
    Attribute attribute = mock(Attribute.class);
    when(tile.getAttribute()).thenReturn(attribute);

    sut.scan(tile);

    assertEquals(attribute, sut.getOutput());
  }
}
