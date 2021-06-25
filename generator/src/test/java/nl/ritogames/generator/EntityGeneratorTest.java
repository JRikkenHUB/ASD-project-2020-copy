package nl.ritogames.generator;

import nl.ritogames.generator.entity.AttributeBuilder;
import nl.ritogames.generator.entity.MobBuilder;
import nl.ritogames.generator.entity.PlayerBuilder;
import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.exception.AbsentEntityException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityGeneratorTest {

    ArrayList<Character> characters;
    int seed;
    int worldSize;
    int difficulty;
    double spawnfactorMob;
    double spawnfactorAtt;
    EntityGenerator sut;
    WorldBench bench;
    AttributeBuilder attributeBuilder;
    MobBuilder mobBuilder;
    PlayerBuilder playerBuilder;


    @BeforeEach
    void setUp() {
        seed = 100;
        worldSize = 200;
        spawnfactorMob = 0.5;
        spawnfactorAtt = 0.7;
        difficulty = 1;
        characters = new ArrayList<>();
        bench = new WorldBench(worldSize);
        attributeBuilder = Mockito.mock(AttributeBuilder.class);
        mobBuilder = Mockito.mock(MobBuilder.class);
        playerBuilder = Mockito.mock(PlayerBuilder.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCorrectBuildersCalled() throws AbsentEntityException {
        sut = new EntityGenerator(mobBuilder, attributeBuilder, playerBuilder);
        double[] mobFactors = {0.5,0.55,0.6};
        double[] attFactors = {0.7,0.5,0.45};
        for (int i = 0; i < 3; i++) {
            Mockito.when(attributeBuilder.build(bench, seed + 1, attFactors[i])).thenReturn(bench);
            Mockito.when(mobBuilder.build(bench, seed - 1, mobFactors[i])).thenReturn(bench);
            int diff = i + 1;
            sut.generateEntities(bench, seed, diff, characters);
            Mockito.verify(attributeBuilder).build(bench, seed + 1, attFactors[i]);
            Mockito.verify(mobBuilder).build(bench, seed - 1, mobFactors[i]);
        }
    }

    @Test
    void testGetterSetterMobBuilder() {
        sut = new EntityGenerator(mobBuilder, attributeBuilder, playerBuilder);
        sut.setMobBuilder(mobBuilder);
        assertEquals(mobBuilder,sut.getMobBuilder());
    }

    @Test
    void testGetterSetterAttributeBuilder() {
        sut = new EntityGenerator(mobBuilder, attributeBuilder, playerBuilder);
        sut.setAttributeBuilder(attributeBuilder);
        assertEquals(attributeBuilder,sut.getAttributeBuilder());
    }

    @Test
    void testGetterSetterPlayerBuilder() {
        sut = new EntityGenerator(mobBuilder, attributeBuilder, playerBuilder);
        sut.setPlayerBuilder(playerBuilder);
        assertEquals(playerBuilder, sut.getPlayerBuilder());
    }
}
