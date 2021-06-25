package nl.ritogames.generator;

import nl.ritogames.generator.entity.AttributeBuilder;
import nl.ritogames.generator.entity.MobBuilder;
import nl.ritogames.generator.entity.PlayerBuilder;
import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.exception.AbsentEntityException;

import java.util.ArrayList;

public class EntityGenerator {
    private MobBuilder mobBuilder;
    private PlayerBuilder playerBuilder;
    private AttributeBuilder attributeBuilder;

    public EntityGenerator() {
        mobBuilder = new MobBuilder();
        playerBuilder = new PlayerBuilder();
        attributeBuilder = new AttributeBuilder();
    }

    public EntityGenerator(MobBuilder mobBuilder, AttributeBuilder attributeBuilder, PlayerBuilder playerBuilder) {
        this.mobBuilder = mobBuilder;
        this.playerBuilder = playerBuilder;
        this.attributeBuilder = attributeBuilder;
    }

    /**
     * Places entities on accessible tiles based on a noise map and spawn rate
     *
     * @param bench                     The bench contains the world as it is being generated.
     * @param seed                      A unique integer that is used to generate a persistent world.
     * @param difficulty                Either 1, 2 or 3. This integer dictates the amount of
     *                                  attributes and monsters that spawn.
     * @param characters                ArrayList of players that have to be placed in the world.
     * @return                          A bench with new entities placed on tiles.
     * @throws AbsentEntityException    Thrown in build methods when a null object is placed on a tile
     */
    public WorldBench generateEntities(WorldBench bench, long seed, int difficulty, ArrayList<Character> characters) throws AbsentEntityException {
        //difficulty: 1-3
        double[] mobSpawnFactors = {0.5,0.5,0.55,0.6}; //index 0 are default
        double[] attributeSpawnFactors = {0.5,0.7,0.5,0.45}; //index 0 are default
        bench = mobBuilder.build(bench, seed - 1, mobSpawnFactors[difficulty]);
        bench = attributeBuilder.build(bench, seed + 1, attributeSpawnFactors[difficulty]);
        bench = playerBuilder.build(bench, seed + 2, characters);
        return bench;
    }

    /**
     * gets the current mobBuilder
     *
     * @return current mobBuilder
     */
    public MobBuilder getMobBuilder() {
        return mobBuilder;
    }

    /**
     * sets a new mobBuilder
     *
     * @param mobBuilder new MobBuilder
     */
    public void setMobBuilder(MobBuilder mobBuilder) {
        this.mobBuilder = mobBuilder;
    }

    /**
     * gets the current AttributeBuilder
     *
     * @return current AttributeBuilder
     */
    public AttributeBuilder getAttributeBuilder() {
        return attributeBuilder;
    }

    /**
     * sets a new AttributeBuilder
     *
     * @param attributeBuilder new AttributeBuilder
     */
    public void setAttributeBuilder(AttributeBuilder attributeBuilder) {
        this.attributeBuilder = attributeBuilder;
    }

    /**
     * gets the current PlayerBuilder
     *
     * @return current PlayerBuilder
     */
    public PlayerBuilder getPlayerBuilder() {
        return playerBuilder;
    }

    /**
     * sets a new PlayerBuilder
     *
     * @param playerBuilder new PlayerBuilder
     */
    public void setPlayerBuilder(PlayerBuilder playerBuilder) {
        this.playerBuilder = playerBuilder;
    }
}
