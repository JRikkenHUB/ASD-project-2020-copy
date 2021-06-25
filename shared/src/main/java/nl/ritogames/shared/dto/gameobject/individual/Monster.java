package nl.ritogames.shared.dto.gameobject.individual;

import nl.ritogames.shared.dto.gameobject.ASDVector;

public class Monster extends Individual {

  /**
   * The individual type monster.
   */
  private String name;
  private char texture;

    /**
     * The Monster class are used for the monsters that randomly spawn in the world.
     */
    public Monster() {
    }

  public Monster(int x, int y, int maxHp, int dmg, int def, String name, char texture) {
    super(x, y, maxHp, dmg, def);
    this.name = name;
    this.texture = texture;
  }

  public Monster(ASDVector location, int maxHp, int dmg, int def, String name, char texture) {
    super(location, maxHp, dmg, def);
    this.name = name;
    this.texture = texture;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public char getTexture() {
    return texture;
  }

  public void setTexture(char texture) {
    this.texture = texture;
  }
}
