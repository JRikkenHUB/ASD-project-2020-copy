package nl.ritogames.shared.dto.gameobject.trap;

/**
 * Abstract class for the traps in the game.
 */
public class Trap {

  String name;
  boolean visible;
  int damage;
  double spawnRate;
  char texture;

  public Trap(String name, Boolean visible, int damage, double spawnRate, char texture) {
    this.name = name;
    this.visible = visible;
    this.damage = damage;
    this.spawnRate = spawnRate;
    this.texture = texture;
  }

  public Trap() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public double getSpawnRate() {
    return spawnRate;
  }

    public void setSpawnRate(int spawnRate) { this.spawnRate = spawnRate; }

  public void setTexture(char texture) {
    this.texture = texture;
  }

  public char getTexture() {
      return texture;
  }

  public String toString() {
    return ("name: " + name + "\nvisible: " + visible + "\ndamage: " + damage + "\nrarity: "
        + spawnRate + "\ntexture: " + texture);
  }
}
