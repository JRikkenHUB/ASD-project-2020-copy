package nl.ritogames.generator.world;

import nl.ritogames.shared.dto.gameobject.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class WorldBenchTest {

    private WorldBench sut;
    private World mockWorld;

    @BeforeEach
    void setUp(){
        mockWorld = mock(World.class);
        sut = new WorldBench(100);
    }

    @Test
    void getRoomsReturnsListOfRooms(){
        List<Room> expectedRooms = new ArrayList<>();
        assertEquals(expectedRooms.getClass(), sut.getRooms().getClass());
    }

    @Test
    void setRoomsSetsCorrectRoomsAndGetRoomsReturnsThem(){
        List<Room> expectedRooms = new ArrayList<>();
        sut.setRooms(expectedRooms);
        assertEquals(expectedRooms, sut.getRooms());
    }

    @Test
    void getPathsReturnsListOfPaths(){
        List<Path> expectedPaths = new ArrayList<>();
        assertEquals(expectedPaths.getClass(), sut.getPaths().getClass());
    }

    @Test
    void setPathsSetsCorrectPathsAndGetPathsReturnsThem(){
        List<Path> expectedPaths = new ArrayList<>();
        sut.setPaths(expectedPaths);
        assertEquals(expectedPaths, sut.getPaths());
    }

    @Test
    void addPathAddsPathAndGetPathsReturnsThemCorrectly(){
        List<Path> expectedPaths = new ArrayList<>();
        Path mockPath = mock(Path.class);
        expectedPaths.add(mockPath);

        sut.addPath(mockPath);
        assertEquals(expectedPaths, sut.getPaths());
    }

    @Test
    void addRoomAdsRoomAndGetRoomsReturnsThemCorrectly(){
        List<Room> expectedRooms = new ArrayList<>();
        Room mockRoom = mock(Room.class);
        expectedRooms.add(mockRoom);

        sut.addRoom(mockRoom);
        assertEquals(expectedRooms, sut.getRooms());
    }
}
