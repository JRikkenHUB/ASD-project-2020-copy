package nl.ritogames.generator.grid;

import nl.ritogames.generator.world.Room;
import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathBuilderTest {
    final int worldSize = 20;
    WorldBench worldBench;

    PathBuilder pathBuilder;

    @BeforeEach
    void setup() {
        worldBench = new WorldBench(worldSize);
        addTiles(worldBench);
        addRooms(worldBench);

        pathBuilder = new PathBuilder();
    }

    @Test
    void testAddPath() {
        pathBuilder.addPaths(worldBench);

        assertEquals(1, worldBench.getPaths().size());
    }

    private void addTiles(WorldBench worldBench) {
        WorldTile[][] worldTiles = new WorldTile[worldSize][worldSize];
        for(int x = 0; x < worldSize; x++) {
            for(int y = 0; y < worldSize; y++) {
                worldTiles[x][y] = new InaccessibleWorldTile(x,y);
            }
        }
        worldBench.setTiles(worldTiles);
    }

    private void addRooms(WorldBench worldBench) {
        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room(4,4,4,4,1));
        roomList.add(new Room(16,16,4,4,1));

        for(Room room: roomList) {
            placeRoom(room, worldBench);
        }

        worldBench.setRooms(roomList);
    }

    private void placeRoom(Room room, WorldBench bench) {
        for (int x = 0; x < room.width; x++) {
            for (int y = 0; y < room.height; y++) {
                int xCoord = x - Math.round(room.width / 2) + room.getX();
                int yCoord = y - Math.round(room.height / 2) + room.getY();

                if (bench.isValidCoordinate(xCoord, yCoord)) {
                    WorldTile worldTile = new AccessibleWorldTile(xCoord,yCoord);
                    room.getTiles().add(worldTile);
                    bench.setTile(worldTile);
                }
            }
        }
    }
}