package nl.ritogames.trui.uicomponent.worldview.ruleengine;

import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;

public class WorldViewExpression {

  private ASDTile tile;

  public WorldViewExpression(ASDTile tile) {
    this.tile = tile;
  }

  public ASDTile getTile() {
    return tile;
  }

  public void setTile(ASDTile tile) {
    this.tile = tile;
  }
}
