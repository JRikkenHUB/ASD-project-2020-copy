package nl.ritogames.generator.world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RoomTest {

    Room sut;

    @BeforeEach
    void setUp(){
        sut = new Room(1,1,1,1,1);
    }

    @Test
    void placeRoomCallsIsValidCoordFromWorld(){
        WorldBench mockWorldBench = mock(WorldBench.class);
        when(mockWorldBench.isValidCoordinate(1,1)).thenReturn(true);
        sut.placeRoom(mockWorldBench);

        verify(mockWorldBench).isValidCoordinate(1,1);
    }
}
