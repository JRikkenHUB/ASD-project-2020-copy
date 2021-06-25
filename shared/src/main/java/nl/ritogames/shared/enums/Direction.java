package nl.ritogames.shared.enums;

import java.util.NoSuchElementException;

/**
 * The enum Direction.
 */
public enum Direction {
  NORTH,
  SOUTH,
  WEST,
  EAST,
  RANDOM;

    public static Direction getDirection(String dir) {
        String dirLower = dir.toLowerCase();
        return switch (dirLower) {
            case "up", "north" -> Direction.NORTH;
            case "right", "east" -> Direction.EAST;
            case "down", "south" -> Direction.SOUTH;
            case "left", "west" -> Direction.WEST;
            default -> throw new IllegalArgumentException();
        };
    }


  /**
   * Convert String to Direction enum
   *
   * @param dir the string to convert
   * @return the direction enum
   * @throws IllegalArgumentException when provided string can not be converted.
   */
  public static Direction fromString(String dir) {
    return Direction.valueOf(dir.toUpperCase());
  }

  /**
   * Gives the opposite direction, e.g. the opposite of NORTH is SOUTH
   *
   * @return the opposite direction.
   * @throws NoSuchElementException when trying to get the opposite of RANDOM
   * @see Direction
   */
  public Direction opposite() {
    return switch (this) {
      case NORTH -> Direction.SOUTH;
      case EAST -> Direction.WEST;
      case SOUTH -> Direction.NORTH;
      case WEST -> Direction.EAST;
      case RANDOM -> throw new NoSuchElementException(this.name() + " has no opposite.");
    };
  }
}
