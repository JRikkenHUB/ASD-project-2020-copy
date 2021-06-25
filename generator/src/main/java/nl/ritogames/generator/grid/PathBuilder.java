package nl.ritogames.generator.grid;

import nl.ritogames.generator.world.Path;
import nl.ritogames.generator.world.Room;
import nl.ritogames.generator.world.WorldBench;

import java.util.ArrayList;
import java.util.List;

/**
 * The PathBuilder generates paths between generated rooms
 */
public class PathBuilder {
    public static final int ACCEPTABLE_PATH_DISTANCE = 5;

    /**
     * Adds paths to the world in the WorldBench
     *
     * @param worldBench the WorldBench that contains the world
     * @return the WorldBench that contains the paths
     */
    public WorldBench addPaths(WorldBench worldBench) {
        worldBench.setPaths(new ArrayList<>());
        List<Room> nonConnectedRooms = new ArrayList<>(worldBench.getRooms());
        List<Room> connectedRooms = new ArrayList<>();

        connectedRooms.add(nonConnectedRooms.get(nonConnectedRooms.size() / 2));
        nonConnectedRooms.remove(nonConnectedRooms.size() / 2);

        while (!nonConnectedRooms.isEmpty()) {
            Room room = connectedRooms.get(connectedRooms.size() - 1);
            Room closestRoom = getClosestRoom(room, nonConnectedRooms, ACCEPTABLE_PATH_DISTANCE);

            connectedRooms.add(closestRoom);
            nonConnectedRooms.remove(closestRoom);

            worldBench.addPath(new Path(room, closestRoom));
        }

        placePaths(worldBench);
        return worldBench;
    }

    /**
     * Places the paths on the WorldBench.
     *
     * @param bench The WorldBench on which to place the paths.
     */
    private void placePaths(WorldBench bench) {
        for (Path path : bench.getPaths()) path.placePath(bench);
    }

    /**
     * Gets the closest room from a list of rooms
     * @param room Room that is used to calculate the distances
     * @param connectionCandidates List of possible rooms
     * @param acceptablePathDistance If the distances is < then the acceptablePathDistance it will return the Room since it is close enough.
     *                               This is a small optimization so that it doesn't continue searching if it finds a room that is pretty close already.
     * @return
     */
    private Room getClosestRoom(Room room, List<Room> connectionCandidates, int acceptablePathDistance) {
        int closestDistance = 0;
        Room closestRoom = null;

        for (Room r : connectionCandidates) {
            int distance = (Math.abs(room.getX() - r.getX()) + Math.abs(room.getY() - r.getY()));

            if (distance < closestDistance || closestRoom == null) {
                closestRoom = r;
                closestDistance = distance;
            }

            if (closestDistance <= acceptablePathDistance) {
                return closestRoom;
            }
        }

        return closestRoom;
    }
}
