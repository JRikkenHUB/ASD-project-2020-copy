package nl.ritogames.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ASDGeneratorTest {
    ASDGenerator sut;

    @BeforeEach
    void setup() {
        GridGenerator gridGeneratorMock = Mockito.mock(GridGenerator.class);
        EntityGenerator entityGenerator = Mockito.mock(EntityGenerator.class);
        sut = new ASDGenerator(gridGeneratorMock, entityGenerator);
    }

    @Test
    void generateSeedReturnsUniqueSeed() {
        Set<Long> set = new HashSet<>();
        for (int i = 0; i < 500; i++) {
            Assertions.assertDoesNotThrow(() -> set.add(sut.generateSeed()));
        }
    }

    @Test
    void calculateWorldSizeReturnsMinWorldSize(){
        int testAmountOfPlayers = 2;
        int testTilesPerPlayer = 25;
        int minWorldSize = 17;

        assertEquals(minWorldSize, sut.calculateWorldSize(testAmountOfPlayers, testTilesPerPlayer));
    }

    @Test
    void calculateWorldSizeReturnsMaxWorldSize(){
        int testAmountOfPlayers = 50;
        int testTilesPerPlayer = 50;
        int maxWorldSize = 69;

        assertEquals(maxWorldSize, sut.calculateWorldSize(testAmountOfPlayers, testTilesPerPlayer));
    }

    @Test
    void calculateWorldSizeReturnsCorrectWorldSize(){
        int testAmountOfPlayers = 25;
        int testTilesPerPlayer = 30;
        int expectedResult = (int)Math.round(Math.sqrt((testAmountOfPlayers * testTilesPerPlayer)/0.45)*0.995);

        assertEquals(expectedResult, sut.calculateWorldSize(testAmountOfPlayers, testTilesPerPlayer));
    }
}