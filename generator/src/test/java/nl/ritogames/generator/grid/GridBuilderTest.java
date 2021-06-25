package nl.ritogames.generator.grid;

import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GridBuilderTest {
    int worldSize;
    WorldBench bench;
    GridBuilder sut;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddGrid() {
        worldSize = 2;
        bench = new WorldBench(worldSize);
        sut = new GridBuilder();

        WorldTile[][] worldTiles = new WorldTile[worldSize][worldSize];
        worldTiles[0][0] = new InaccessibleWorldTile(0, 0);
        worldTiles[0][1] = new InaccessibleWorldTile(0, 1);
        worldTiles[1][0] = new InaccessibleWorldTile(1, 0);
        worldTiles[1][1] = new InaccessibleWorldTile(1, 1);

        bench = sut.addGrid(bench);

        assertEquals(worldSize,bench.getTiles().length);
    }
}