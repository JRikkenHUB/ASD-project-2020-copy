package nl.ritogames.generator;

import nl.ritogames.generator.grid.GridBuilder;
import nl.ritogames.generator.grid.PathBuilder;
import nl.ritogames.generator.grid.RoomBuilder;
import nl.ritogames.generator.grid.TrapBuilder;
import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.generator.xml.XMLRetriever;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.TrapTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GridGeneratorTest {
    GridGenerator sut;
    WorldBench bench;
    int seed;
    int worldSize;
    XMLRetriever xmlRetriever;


    @BeforeEach
    void setUp() {
        seed = 100;
        worldSize = 200;
        sut = new GridGenerator();
        bench = new WorldBench(worldSize);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGridIsConsistent() throws IOException {
        seed = 10110;
        worldSize = 35;
        sut = new GridGenerator();
        bench = new WorldBench(worldSize);
        String fileName = "traps.xml";

        xmlRetriever = Mockito.mock(XMLRetriever.class);
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

        sut.getTrapBuilder().setXmlRetriever(xmlRetriever);
        
        assertEquals(sut.generateGrid(bench, seed), sut.generateGrid(bench, seed));

        for (WorldTile[] tiles : bench.getTiles()) {
            StringBuilder builder = new StringBuilder();
            for (WorldTile tile : tiles) {
                char texture = 'd';
                if (tile instanceof TrapTile) {
                    texture = ((TrapTile) tile).getTrap().getTexture();
                }
                builder.append((tile instanceof InaccessibleWorldTile) ? " # " : (tile instanceof TrapTile) ? " " + texture + " " : (tile instanceof AccessibleWorldTile) ? " . " : "=");
            }
            System.out.println(builder.toString() + " " + builder.length());

        }
        System.out.println(bench.getTiles().length + " : " + bench.getTiles()[0].length);

        assertEquals(sut.generateGrid(bench, seed), sut.generateGrid(bench, seed));
    }

    @Test
    void testGenerateGridCallsPathBuilder() {
        seed = 100;
        worldSize = 200;
        sut = new GridGenerator();
        bench = new WorldBench(worldSize);

        //prepare
        GridBuilder gridBuilderMock = Mockito.mock(GridBuilder.class);
        RoomBuilder roomBuilderMock = Mockito.mock(RoomBuilder.class);
        PathBuilder pathBuilderMock = Mockito.mock(PathBuilder.class);
        TrapBuilder trapBuilderMock = Mockito.mock(TrapBuilder.class);

        Mockito.when(gridBuilderMock.addGrid(bench)).thenReturn(bench);
        Mockito.when(roomBuilderMock.addRooms(bench, seed, 1)).thenReturn(bench);
        Mockito.when(pathBuilderMock.addPaths(bench)).thenReturn(bench);
        Mockito.when(trapBuilderMock.addTraps(bench, seed, 1)).thenReturn(bench);

        sut.setGridBuilder(gridBuilderMock);
        sut.setPathBuilder(pathBuilderMock);
        sut.setRoomBuilder(roomBuilderMock);
        sut.setTrapBuilder(trapBuilderMock);

        //act
        sut.generateGrid(bench, seed);

        //verify
        Mockito.verify(pathBuilderMock).addPaths(bench);
    }

    @Test
    void testGenerateGridCallsRoomBuilder() {
        seed = 100;
        worldSize = 200;
        sut = new GridGenerator();
        bench = new WorldBench(worldSize);

        //prepare
        GridBuilder gridBuilderMock = Mockito.mock(GridBuilder.class);
        RoomBuilder roomBuilderMock = Mockito.mock(RoomBuilder.class);
        PathBuilder pathBuilderMock = Mockito.mock(PathBuilder.class);
        TrapBuilder trapBuilderMock = Mockito.mock(TrapBuilder.class);

        Mockito.when(gridBuilderMock.addGrid(bench)).thenReturn(bench);
        Mockito.when(roomBuilderMock.addRooms(bench, seed, 1)).thenReturn(bench);
        Mockito.when(pathBuilderMock.addPaths(bench)).thenReturn(bench);
        Mockito.when(trapBuilderMock.addTraps(bench, seed, 1)).thenReturn(bench);

        sut.setGridBuilder(gridBuilderMock);
        sut.setPathBuilder(pathBuilderMock);
        sut.setRoomBuilder(roomBuilderMock);
        sut.setTrapBuilder(trapBuilderMock);


        //act
        sut.generateGrid(bench, seed);

        //verify
        Mockito.verify(roomBuilderMock).addRooms(bench, seed, 1);
    }

    @Test
    void testGenerateGridCallsGridBuilder() {
        seed = 100;
        worldSize = 200;
        sut = new GridGenerator();
        bench = new WorldBench(worldSize);


        //prepare
        GridBuilder gridBuilderMock = Mockito.mock(GridBuilder.class);
        RoomBuilder roomBuilderMock = Mockito.mock(RoomBuilder.class);
        PathBuilder pathBuilderMock = Mockito.mock(PathBuilder.class);
        TrapBuilder trapBuilderMock = Mockito.mock(TrapBuilder.class);

        Mockito.when(gridBuilderMock.addGrid(bench)).thenReturn(bench);
        Mockito.when(roomBuilderMock.addRooms(bench, seed, 1)).thenReturn(bench);
        Mockito.when(pathBuilderMock.addPaths(bench)).thenReturn(bench);
        Mockito.when(trapBuilderMock.addTraps(bench, seed, 1)).thenReturn(bench);

        sut.setGridBuilder(gridBuilderMock);
        sut.setPathBuilder(pathBuilderMock);
        sut.setRoomBuilder(roomBuilderMock);
        sut.setTrapBuilder(trapBuilderMock);


        //act
        sut.generateGrid(bench, seed);

        //verify
        Mockito.verify(gridBuilderMock).addGrid(bench);
    }

    @Test
    void testGenerateGridCallsTrapBuilder() {
        seed = 100;
        worldSize = 200;
        sut = new GridGenerator();
        bench = new WorldBench(worldSize);

        //prepare
        GridBuilder gridBuilderMock = Mockito.mock(GridBuilder.class);
        RoomBuilder roomBuilderMock = Mockito.mock(RoomBuilder.class);
        PathBuilder pathBuilderMock = Mockito.mock(PathBuilder.class);
        TrapBuilder trapBuilderMock = Mockito.mock(TrapBuilder.class);

        Mockito.when(gridBuilderMock.addGrid(bench)).thenReturn(bench);
        Mockito.when(roomBuilderMock.addRooms(bench, seed, 1)).thenReturn(bench);
        Mockito.when(pathBuilderMock.addPaths(bench)).thenReturn(bench);
        Mockito.when(trapBuilderMock.addTraps(bench, seed, 1)).thenReturn(bench);

        sut.setGridBuilder(gridBuilderMock);
        sut.setPathBuilder(pathBuilderMock);
        sut.setRoomBuilder(roomBuilderMock);
        sut.setTrapBuilder(trapBuilderMock);

        //act
        sut.generateGrid(bench, seed);

        //verify
        Mockito.verify(trapBuilderMock).addTraps(bench, seed, 1);
    }

    @Test
    void testGetterAndSetterGridBuilder() {
        //prepare
        sut = new GridGenerator();
        GridBuilder gridBuilderMock = Mockito.mock(GridBuilder.class);

        //act
        sut.setGridBuilder(gridBuilderMock);

        //verify
        assertEquals(gridBuilderMock, sut.getGridBuilder());
    }

    @Test
    void testGetterAndSetterPathBuilder() {
        //prepare
        sut = new GridGenerator();
        PathBuilder pathBuilderMock = Mockito.mock(PathBuilder.class);

        //act
        sut.setPathBuilder(pathBuilderMock);

        //verify
        assertEquals(pathBuilderMock, sut.getPathBuilder());
    }

    @Test
    void testGetterAndSetterRoomBuilder() {
        //prepare
        sut = new GridGenerator();
        RoomBuilder roomBuilderMock = Mockito.mock(RoomBuilder.class);

        //act
        sut.setRoomBuilder(roomBuilderMock);

        //verify
        assertEquals(roomBuilderMock, sut.getRoomBuilder());
    }
}
