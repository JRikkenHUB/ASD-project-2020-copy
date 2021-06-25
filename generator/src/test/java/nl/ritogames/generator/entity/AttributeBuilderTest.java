package nl.ritogames.generator.entity;

import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.generator.xml.XMLRetriever;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.exception.AbsentEntityException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AttributeBuilderTest {
    int worldSize;
    WorldBench bench;
    AttributeBuilder sut;
    XMLRetriever xmlRetriever;
    String path;
    long seed;
    double spawnfactor;

    @BeforeEach
    public void setUp() {
        worldSize = 2;
        seed = 1;
        spawnfactor = 0;
        path = "entities.xml";
        bench = new WorldBench(worldSize);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void testBuild() throws IOException, URISyntaxException, AbsentEntityException {
        xmlRetriever = Mockito.mock(XMLRetriever.class);
        Mockito.when(xmlRetriever.getXMLString(path)).thenReturn("<?xml version=\"1.0\"?>\n" +
                "<entities>\n" +
                "<armourtypes>\n" +
                "    <attribute name=\"diamond chestplate\">\n" +
                "        <type>armour</type>\n" +
                "        <defense>20</defense>\n" +
                "        <spawnrate>0.03</spawnrate>\n" +
                "    </attribute>\n" +
                "</armourtypes>\n" +
                "<weapontypes>\n" +
                "    <attribute name=\"a gun\">\n" +
                "        <type>weapon</type>\n" +
                "        <damage>100</damage>\n" +
                "        <spawnrate>0.001</spawnrate>\n" +
                "    </attribute>\n" +
                "</weapontypes>\n" +
                "<potiontypes>\n" +
                "    <attribute name=\"weird leave\">\n" +
                "        <type>potion</type>\n" +
                "        <characterField>healthBuff</characterField>\n" +
                "        <effect>40</effect>\n" +
                "        <spawnrate>1</spawnrate>\n" +
                "    </attribute>\n" +
                "</potiontypes>\n" +
                "<mobTypes>\n" +
                "    <mob name=\"Wild boar\">\n" +
                "        <damage>20</damage>\n" +
                "        <characterField>healthBuff</characterField>\n" +
                "        <effect>20</effect>\n" +
                "        <spawnrate>1</spawnrate>\n" +
                "        <texture>O</texture>\n" +
                "    </mob>\n" +
                "</mobTypes>\n" +
                "</entities>");
        sut = new AttributeBuilder();
        WorldTile[][] contextTiles = new WorldTile[][]{
                {
                        new AccessibleWorldTile(1, 1),
                        new AccessibleWorldTile(1, 2),
                        new AccessibleWorldTile(1, 3),
                        new AccessibleWorldTile(1, 4),
                        new AccessibleWorldTile(1, 5),
                        new InaccessibleWorldTile(1, 6),
                        new AccessibleWorldTile(1, 7),
                        new InaccessibleWorldTile(1, 8),
                        new InaccessibleWorldTile(1, 9)
                },
                {
                        new AccessibleWorldTile(2, 1),
                        new AccessibleWorldTile(2, 2),
                        new AccessibleWorldTile(2, 3),
                        new AccessibleWorldTile(2, 4),
                        new AccessibleWorldTile(2, 5),
                        new InaccessibleWorldTile(2, 6),
                        new AccessibleWorldTile(2, 7),
                        new InaccessibleWorldTile(2, 8),
                        new InaccessibleWorldTile(2, 9)
                },
                {
                        new AccessibleWorldTile(3, 1),
                        new AccessibleWorldTile(3, 2),
                        new AccessibleWorldTile(3, 3),
                        new AccessibleWorldTile(3, 4),
                        new AccessibleWorldTile(3, 5),
                        new InaccessibleWorldTile(3, 6),
                        new AccessibleWorldTile(3, 7),
                        new InaccessibleWorldTile(3, 8),
                        new InaccessibleWorldTile(3, 9)
                },
                {
                        new AccessibleWorldTile(4, 1),
                        new AccessibleWorldTile(4, 2),
                        new AccessibleWorldTile(4, 3),
                        new AccessibleWorldTile(4, 4),
                        new AccessibleWorldTile(4, 5),
                        new InaccessibleWorldTile(4, 6),
                        new AccessibleWorldTile(4, 7),
                        new InaccessibleWorldTile(4, 8),
                        new InaccessibleWorldTile(4, 9)
                },
                {
                        new AccessibleWorldTile(5, 1),
                        new AccessibleWorldTile(5, 2),
                        new AccessibleWorldTile(5, 3),
                        new AccessibleWorldTile(5, 4),
                        new AccessibleWorldTile(5, 5),
                        new AccessibleWorldTile(5, 6),
                        new InaccessibleWorldTile(5, 7),
                        new AccessibleWorldTile(5, 8),
                        new InaccessibleWorldTile(5, 9)
                },
                {
                        new AccessibleWorldTile(6, 1),
                        new AccessibleWorldTile(6, 2),
                        new AccessibleWorldTile(6, 3),
                        new AccessibleWorldTile(6, 4),
                        new AccessibleWorldTile(6, 5),
                        new AccessibleWorldTile(6, 6),
                        new InaccessibleWorldTile(6, 7),
                        new AccessibleWorldTile(6, 8),
                        new InaccessibleWorldTile(6, 9)
                },
                {
                        new AccessibleWorldTile(7, 1),
                        new AccessibleWorldTile(7, 2),
                        new AccessibleWorldTile(7, 3),
                        new AccessibleWorldTile(7, 4),
                        new AccessibleWorldTile(7, 5),
                        new AccessibleWorldTile(7, 6),
                        new InaccessibleWorldTile(7, 7),
                        new AccessibleWorldTile(7, 8),
                        new InaccessibleWorldTile(7, 9)
                },
                {
                        new AccessibleWorldTile(8, 1),
                        new AccessibleWorldTile(8, 2),
                        new AccessibleWorldTile(8, 3),
                        new AccessibleWorldTile(8, 4),
                        new AccessibleWorldTile(8, 5),
                        new AccessibleWorldTile(8, 6),
                        new InaccessibleWorldTile(8, 7),
                        new AccessibleWorldTile(8, 8),
                        new InaccessibleWorldTile(8, 9)
                },
                {
                        new AccessibleWorldTile(9, 1),
                        new AccessibleWorldTile(9, 2),
                        new AccessibleWorldTile(9, 3),
                        new AccessibleWorldTile(9, 4),
                        new AccessibleWorldTile(9, 5),
                        new AccessibleWorldTile(9, 6),
                        new InaccessibleWorldTile(9, 7),
                        new AccessibleWorldTile(9, 8),
                        new InaccessibleWorldTile(9, 9)
                }
        };

        bench.setTiles(contextTiles);
        sut.build(bench, seed, spawnfactor);

        boolean actual = false;
        for (WorldTile[] tiles: bench.getTiles()) {
            for (WorldTile tile: tiles){
                if(tile instanceof AccessibleWorldTile){
                    actual = ((AccessibleWorldTile) tile).hasAttribute();
                    if (actual){
                        break;
                    }
                }
            }
        }
        assertTrue(actual);
    }
}
