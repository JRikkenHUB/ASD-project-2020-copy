package nl.ritogames.intelligentagent.sensor;

import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;

/** UnitSensor scans the tiles and detects if an unit is available. */
public class UnitSensor extends Sensor<Individual> {

  public UnitSensor(String sensorName) {
    super(sensorName);
  }

  @Override
  public Individual scan(ASDTile tile) {
    if (tile instanceof AccessibleWorldTile) {
      output = ((AccessibleWorldTile) tile).getIndividual();
    }

    return output;
  }
}
