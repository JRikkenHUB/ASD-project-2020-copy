package nl.ritogames.intelligentagent.sensor;

import lombok.Getter;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;

/**
 * Sensor scans the tiles and detects if an object is available.
 *
 * @param <T>
 */
@Getter
public abstract class Sensor<T> {

  protected String sensorName;
  protected T output;

  protected Sensor(String sensorName) {
    this.sensorName = sensorName;
  }

  /**
   * Scan ASDTile for T. When T is found return T.
   *
   * @param tile the ASDTile
   * @return the T if found. else return null.
   */
  public abstract T scan(ASDTile tile);

  /** Clears output. Sets it to null */
  public final void clearOutput() {
    this.output = null;
  }
}
