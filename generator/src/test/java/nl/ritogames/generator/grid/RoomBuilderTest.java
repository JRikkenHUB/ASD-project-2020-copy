package nl.ritogames.generator.grid;

import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomBuilderTest {

    RoomBuilder sut;

    @BeforeEach
    void setUp() {
        sut = new RoomBuilder();
    }

    @Test
    void addRoomsReturnsWorldBench() {
        int worldSize = 2;
        WorldBench bench = new WorldBench(worldSize);
        WorldTile[][] worldTiles = new WorldTile[worldSize][worldSize];

        worldTiles[0][0] = new InaccessibleWorldTile(0, 0);
        worldTiles[0][1] = new InaccessibleWorldTile(0, 1);
        worldTiles[1][0] = new InaccessibleWorldTile(1, 0);
        worldTiles[1][1] = new InaccessibleWorldTile(1, 1);

        bench.setTiles(worldTiles);
        sut.addRooms(bench, 1, 1);
        Assertions.assertEquals(1, bench.getRooms().size());
    }
}
