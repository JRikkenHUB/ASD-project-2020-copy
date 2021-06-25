package nl.ritogames.intelligentagent.sensor;

import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;

/** UnitSensor scans the tiles and detects if an artifact is available. */
public class ArtifactSensor extends Sensor<Attribute> {

  public ArtifactSensor(String sensorName) {
    super(sensorName);
  }

  @Override
  public Attribute scan(ASDTile tile) {
    if (tile instanceof AccessibleWorldTile) {
      output = ((AccessibleWorldTile) tile).getAttribute();
    }

    return output;
  }
}
