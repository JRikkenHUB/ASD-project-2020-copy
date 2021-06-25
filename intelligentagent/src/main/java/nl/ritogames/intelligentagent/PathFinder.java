package nl.ritogames.intelligentagent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import nl.ritogames.intelligentagent.sensor.Sensor;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.exception.OutOfMapException;

/**
 * Simple Breadth-first search PathFinding algorithm based on:
 * https://codereview.stackexchange.com/a/206073 Finds the shortest Path from one position to
 * another. If no path can be found or an error occurs it returns an empty List.
 */
public final class PathFinder {

  private PathFinder() {}

  /**
   * Find from shortest path between two points in the World.
   *
   * <p>When the destination hasn't been reached the List will have a size of >1. When the
   * destination has been reached the List will have a size of 1. When the destination can't be
   * reached the List will have a size of 0.
   *
   * @param tiles A double ASDTile array representing the viewable map.
   * @param position The position to start moving from, usually the characters position.
   * @param destination The destination to move to, usually an Enemy or an Artifact.
   * @return The shortest Path returned as an ASDVector List, containing all steps in order.
   */
  public static List<ASDVector> findPath(
      final ASDTile[][] tiles, final ASDVector position, final ASDVector destination) {
    World world;
    try {
      world = new World(tiles);
    } catch (OutOfMapException exception) {
      return new ArrayList<>();
    }

    // Check if the current position and destination are available.
    if (isNotReachable(position, world) || isNotReachable(destination, world)) {
      return new ArrayList<>();
    }

    // Generate a map to keep track of the cost to reach each tile
    int[][] map = generateMap(world);

    Queue<ASDVector> settledTiles = new ArrayDeque<>();
    Queue<ASDVector> unSettledTiles = new ArrayDeque<>();

    settledTiles.add(position);

    map[position.getX()][position.getY()] = -1;

    for (int i = 2; !settledTiles.isEmpty(); i++) {
      if (settledTiles.size() >= map.length * map[0].length) {
        throw new IllegalStateException("Map overload");
      }

      for (ASDVector checkPos : settledTiles) {
        if (checkPos.equals(destination)) {
          List<ASDVector> solution = arrived(world, map, i - 1, checkPos);
          // Remove first step from the solution since that is the current position
          solution.remove(0);
          return solution;
        }
        if (isAccessible(world, checkPos.getX(), checkPos.getY()) || checkPos.equals(position)) {
          findUnwalkedTiles(map, checkPos, unSettledTiles, i);
        }
      }

      settledTiles = unSettledTiles;
      unSettledTiles = new ArrayDeque<>();
    }

    return new ArrayList<>();
  }

  /**
   * Since the algorithm to properly scan the map is very similar to the PathFinder the Scanner
   * method is also implemented in the PathFinder class.
   *
   * <p>The ScanMap function simply scans the reachable parts of the visible map with the given
   * sensors. It stops once all sensors found something or all the reachable parts of the visible
   * map have been scanned.
   *
   * @param tiles A double ASDTile array representing the viewable map.
   * @param positionVector The position to start moving from, usually the characters position.
   * @param sensors The given sensors looking for something.
   */
  public static void scanMap(
      final ASDTile[][] tiles, final ASDVector positionVector, List<Sensor> sensors) {
    World world;
    try {
      world = new World(tiles);
    } catch (OutOfMapException exception) {
      return;
    }

    // Check if the current position and destination are available.
    if (isNotReachable(positionVector, world)) {
      return;
    }

    // Generate a map to keep track of already checked tiles)
    int[][] map = generateMap(world);

    Queue<ASDVector> settledTiles = new ArrayDeque<>();
    Queue<ASDVector> unSettledTiles = new ArrayDeque<>();

    settledTiles.add(positionVector);
    map[positionVector.getY()][positionVector.getX()] = -1;

    for (int i = 2; !settledTiles.isEmpty(); i++) {

      if (settledTiles.size() >= map.length * map[0].length) {
        throw new IllegalStateException("Map overload");
      }
      boolean sensorsNeedToCheck = false;

      for (ASDVector checkPos : settledTiles) {
        if (!checkPos.equals(positionVector)) {
          sensorsNeedToCheck = checkingSensors(sensors, world, sensorsNeedToCheck, checkPos);
          if (!sensorsNeedToCheck) {
            return;
          }
        }

        if (isAccessible(world, checkPos.getX(), checkPos.getY()) || checkPos
            .equals(positionVector)) {
          findUnwalkedTiles(map, checkPos, unSettledTiles, i);
        }
      }

      settledTiles = unSettledTiles;
      unSettledTiles = new ArrayDeque<>();
    }
  }

  private static boolean checkingSensors(
      List<Sensor> sensors, World world, boolean sensorsNeedToCheck, ASDVector checkPos) {
    for (Sensor sensor : sensors) {
      if (sensor.getOutput() == null) {
        sensor.scan(world.getTile(checkPos.getX(), checkPos.getY()));
        if (sensor.getOutput() == null) {
          sensorsNeedToCheck = true;
        }
      }
    }
    return sensorsNeedToCheck;
  }

  private static int[][] generateMap(World world) {
    int[][] map = new int[world.calcWorldSize()][world.calcWorldSize()];
    for (int[] row : map) {
      Arrays.fill(row, 0);
    }
    for (int x = 0; x < world.calcWorldSize(); x++) {
      for (int y = 0; y < world.calcWorldSize(); y++) {
        if (world.getTile(x, y) instanceof InaccessibleWorldTile) {
          map[x][y] = 1;
        }
      }
    }
    return map;
  }

  private static boolean isNotReachable(ASDVector vector, World world) {
    return isOutOfMap(world, vector.getX(), vector.getY()) || isBlocked(world, vector.getX(), vector.getY());
  }

  private static void findUnwalkedTiles(
      int[][] map, ASDVector checkPos, Queue<ASDVector> finalQueue, int finalStepCount) {
    lookAround(
        map,
        checkPos,
        (x, y) -> {
          if (alreadyChecked(map, x, y)) {
            return;
          }

          ASDVector e = new ASDVector(x, y);

          finalQueue.add(e);

          map[e.getX()][e.getY()] = -finalStepCount;
        });
  }

  private static boolean isOutOfMap(World world, int x, int y) {
    return world.getTile(x, y) == null;
  }

  private static boolean isAccessible(World world, int x, int y) {
    return world.getTile(x, y).tileAccessible();
  }

  private static boolean isBlocked(World world, int x, int y) {
    return !(world.getTile(x, y) instanceof AccessibleWorldTile);
  }

  private static boolean alreadyChecked(int[][] map, int x, int y) {
    return map[x][y] != 0;
  }

  private static List<ASDVector> arrived(
      final World world, final int[][] map, final int size, final ASDVector p) {
    final ASDVector[] optimalPath = new ASDVector[size];

    computeSolution(world, map, p.getX(), p.getY(), size, optimalPath);

    return new LinkedList<>(Arrays.asList(optimalPath));
  }

  private static void computeSolution(
      final World world,
      final int[][] map,
      final int x,
      final int y,
      final int stepCount,
      final ASDVector[] optimalPath) {
    if (isOutOfMap(world, x, y) || map[x][y] == 0 || map[x][y] != -stepCount) {
      return;
    }

    final ASDVector p = new ASDVector(x, y);

    optimalPath[stepCount - 1] = p;

    lookAround(map, p, (x1, y1) -> computeSolution(world, map, x1, y1, stepCount - 1, optimalPath));
  }

  private static void lookAround(final int[][] map, final ASDVector p, final Callback callback) {
    callback.look(map, p.getX() + 1, p.getY());
    callback.look(map, p.getX() - 1, p.getY());
    callback.look(map, p.getX(), p.getY() + 1);
    callback.look(map, p.getX(), p.getY() - 1);
  }

  private interface Callback {

    default void look(final int[][] map, final int x, final int y) {
      if (x < 0 || y < 0 || map.length <= y || map[0].length <= x) {
        return;
      }
      onLook(x, y);
    }

    void onLook(int x, int y);
  }
}
