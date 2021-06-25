package nl.ritogames.generator.grid;

import com.raylabz.opensimplex.OpenSimplexNoise;
import nl.ritogames.generator.world.Room;
import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;

import java.util.Random;

/**
 * The RoomBuilder places rooms in the world
 */
public class RoomBuilder {
    public static final int MAX_ROOM_SIZE = 3;
    public static final double ROOM_SPARSITY = 0.45;

    /**
     * AddRooms generates rooms based on noise.
     *
     * @param bench            the WorldBench that contains the world.
     * @param seed             the seed used to generate the noise.
     * @param noiseFeatureSize the scale of the noise map.
     * @return the WorldBench containing a world with rooms.
     */
    public WorldBench addRooms(WorldBench bench, long seed, int noiseFeatureSize) {
        OpenSimplexNoise noise = getNoise(seed, noiseFeatureSize);
        placeTilesWhenSuited(bench, noise);
        placeRooms(bench);
        return bench;
    }

    /**
     * Places tiles in the WorldBench
     *
     * @param bench The WorldBench that contains the world.
     * @param noise The noise which determines where to place which tiles.
     */
    private void placeTilesWhenSuited(WorldBench bench, OpenSimplexNoise noise) {
        for (int x = 0; x < bench.getWorldSize(); x++) {
            for (int y = 0; y < bench.getTiles()[x].length; y++) {
                placeTileWhenSuited(bench, noise, bench.getTile(x, y), ROOM_SPARSITY);
            }
        }
    }

    /**
     * Places rooms in the WorldBench
     *
     * @param bench The WorldBench that contains the world.
     */
    private void placeRooms(WorldBench bench) {
        for (Room room : bench.getRooms()) room.placeRoom(bench);
    }

    /**
     * Generates noise based on a seed and the feature size of the noise.
     *
     * @param seed              The seed to use when generating the noise.
     * @param noiseFeatureSize  The feature size of the noise.
     */
    private OpenSimplexNoise getNoise(long seed, int noiseFeatureSize) {
        OpenSimplexNoise noise = new OpenSimplexNoise(seed);
        noise.setFeatureSize(noiseFeatureSize);
        return noise;
    }

    /**
     * Places a single tile in the WorldBench.
     *
     * @param bench         The WorldBench that contains the world.
     * @param noise         The noise which determines where to place which tiles.
     * @param tile          The tile that should be placed in the WorldBench
     * @param roomSparsity  The sparsity of the rooms in the WorldBench.
     */
    private void placeTileWhenSuited(WorldBench bench, OpenSimplexNoise noise, WorldTile tile, double roomSparsity) {
        double noiseValue = noise.getNoise2D(tile.getCoordinates().getX(), tile.getCoordinates().getY()).getValue();
        if (noiseValue > roomSparsity) {
            createRoom(tile.getCoordinates().getX(), tile.getCoordinates().getY(), noiseValue, bench);
        }
    }

    /**
     * Creates a rooms in the WorldBench
     *
     * @param x             The x location of the room.
     * @param y             The y location of the room.
     * @param  noise        The noise of the World
     * @param  worldBench   The WorldBench that contains the world.
     */
    private void createRoom(int x, int y, double noise, WorldBench worldBench) {
        Random random = new Random((long) ((x + y + noise) * 1000));
        Room room = new Room(x, y, generateWidthOrHeight(random, MAX_ROOM_SIZE), generateWidthOrHeight(random, MAX_ROOM_SIZE), (float) noise);
        worldBench.addRoom(room);
    }

    /**
     * Generates a random witdth or height for a room.
     *
     * @param random        The random to use when generating the size.
     * @param maxRoomSize   The maximum room size.
     * @return              The randomly generated width or height.
     */
    private int generateWidthOrHeight(Random random, int maxRoomSize) {
        return Math.round((random.nextFloat() * ((maxRoomSize - 2) + 1)) + 2);
    }
}
