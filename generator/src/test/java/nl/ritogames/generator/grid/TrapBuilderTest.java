package nl.ritogames.generator.grid;

import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.generator.xml.XMLRetriever;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.TrapTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrapBuilderTest {

    TrapBuilder sut;
    WorldBench bench;
    WorldTile[][] worldTiles;
    XMLRetriever xmlRetriever;
    String fileName;

    int worldSize = 4;

    @BeforeEach
    void setUp() throws IOException {
        worldTiles = getAccessibleTiles(worldSize);

        bench = new WorldBench(worldSize);
        bench.setTiles(worldTiles);

        xmlRetriever = Mockito.mock(XMLRetriever.class);
        fileName = "traps.xml";

        Mockito.when(xmlRetriever.getXMLString(fileName)).thenReturn("<?xml version=\"1.0\"?>\n" +
                "<traps>\n" +
                "    <trap name=\"thornbush\">\n" +
                "        <visible>true</visible>\n" +
                "        <damage>20</damage>\n" +
                "        <spawnrate>20</spawnrate>\n" +
                "        <texture>X</texture>\n" +
                "    </trap>\n" +
                "    <trap name=\"pit\">\n" +
                "        <visible>false</visible>\n" +
                "        <damage>15</damage>\n" +
                "        <spawnrate>15</spawnrate>\n" +
                "        <texture>T</texture>\n" +
                "    </trap>\n" +
                "</traps>");

        sut = new TrapBuilder();
        sut.setXmlRetriever(xmlRetriever);
    }

    @Test
    void addTrapsReturnsWorldBenchWithTraps() {
        sut.addTraps(bench, 10110, 1);
        assertTrue(containsTraps());
    }

    @Test
    void addTrapsShouldReturnWorldBenchWithoutTraps() throws IOException {
        Mockito.when(xmlRetriever.getXMLString(fileName)).thenReturn("<?xml version=\"1.0\"?>\n" +
                "<traps>\n" +
                "</traps>");

        sut.addTraps(bench, 10110, 1);

        assertTrue(containsTraps());
    }

    @Test
    void trapBuilderDoesNotReturnDefaultTrapsWhenCalledMultipleTimes() {
        sut.addTraps(bench, 2145645734, 1);
        sut.addTraps(bench, 2145645734, 1);

        boolean containsDefaultTraps = false;
        for (WorldTile[] tiles : bench.getTiles()) {
            for (WorldTile tile : tiles) {
                if (tile instanceof TrapTile) {
                    if (((TrapTile) tile).getTrap().getTexture() == 'd') {
                        System.out.println(((TrapTile) tile).getTrap().getTexture());
                        containsDefaultTraps = true;
                        break;
                    }
                }
            }
        }

        assertFalse(containsDefaultTraps);
    }

    @Test
    void trapBuilderDoesNotReturnDefaultTrapsWhenCalledOnce() {
        sut.addTraps(bench, 2145645734, 1);

        boolean containsDefaultTraps = false;
        for (WorldTile[] tiles : bench.getTiles()) {
            for (WorldTile tile : tiles) {
                if (tile instanceof TrapTile) {
                    if (((TrapTile) tile).getTrap().getTexture() == 'd') {
                        System.out.println(((TrapTile) tile).getTrap().getTexture());
                        containsDefaultTraps = true;
                        break;
                    }
                }
            }
        }

        assertFalse(containsDefaultTraps);
    }

    @Test
    void addTrapsOnlyPlacesTrapsOnAccessibleWorldTiles() {
        worldTiles = getInaccessibleTiles();
        bench.setTiles(worldTiles);

        sut.addTraps(bench, 10110, 1);
        Assertions.assertFalse(containsTraps());
    }

    private boolean containsTraps() {
        boolean containsTraps = false;
        for (WorldTile[] tiles : bench.getTiles()) {
            for (WorldTile tile : tiles) {
                if (tile instanceof TrapTile) {
                    containsTraps = true;
                    break;
                }
            }
        }
        return containsTraps;
    }

    private WorldTile[][] getInaccessibleTiles() {
        WorldTile[][] worldTiles = new WorldTile[worldSize][worldSize];

        for (int i = 0; i < worldSize; i++) {
            for (int j = 0; j < worldSize; j++) {
                worldTiles[i][j] = new InaccessibleWorldTile(i, j);
            }
        }

        return worldTiles;
    }

    private WorldTile[][] getAccessibleTiles(int worldSize) {
        WorldTile[][] worldTiles = new WorldTile[worldSize][worldSize];

        for (int i = 0; i < worldSize; i++) {
            for (int j = 0; j < worldSize; j++) {
                worldTiles[i][j] = new AccessibleWorldTile(i, j);
            }
        }

        return worldTiles;
    }
}
