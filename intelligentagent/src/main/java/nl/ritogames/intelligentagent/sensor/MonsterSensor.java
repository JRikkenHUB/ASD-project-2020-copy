package nl.ritogames.intelligentagent.sensor;

import nl.ritogames.shared.dto.gameobject.individual.Monster;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;

/**
 * A sensor that scans for nearby monsters.
 */
public class MonsterSensor extends Sensor<Monster> {

  public MonsterSensor(String sensorName) {
    super(sensorName);
  }

  @Override
  public Monster scan(ASDTile tile) {
    if (tile instanceof AccessibleWorldTile) {
      AccessibleWorldTile accessibleWorldTile = (AccessibleWorldTile) tile;
      if (accessibleWorldTile.getIndividual() instanceof Monster) {
        output = (Monster) accessibleWorldTile.getIndividual();
      }
    }

    return output;
  }
}
