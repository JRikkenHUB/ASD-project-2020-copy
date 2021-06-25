package nl.ritogames.trui.uicomponent;

import java.util.Objects;

/**
 * <p>A simple data structure to hold x and y values</p>
 */
public class Vector {

  public static final Vector ZERO = new Vector(0, 0);

  private final int x;
  private final int y;

  public Vector(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vector vector = (Vector) o;
    return Float.compare(vector.x, x) == 0 &&
        Float.compare(vector.y, y) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "Vector{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }

}
