package nl.ritogames.generator.world;

import nl.ritogames.generator.grid.GridBuilder;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathTest {
    private Path sut;
    private Room a;
    private Room b;
    private WorldBench bench;

    @BeforeEach
    void setUp(){
        bench = new WorldBench(100);
        new GridBuilder().addGrid(bench);
        a = new Room(1,1,2,2,1);
        b = new Room(5,5,2,2,1);
        a.placeRoom(bench);
        b.placeRoom(bench);
        sut = new Path(a, b);
    }

    @Test
    void testPlaceRoom(){
        sut.placePath(bench);
        int expected = 15;
        int count = 0;
        for (WorldTile[] tiles: bench.getTiles()) {
            for (WorldTile tile:tiles) {
                count += (tile instanceof AccessibleWorldTile)? 1 : 0;
            }
        }
        assertEquals(expected, count);
    }
}
