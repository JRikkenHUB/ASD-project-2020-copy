package nl.ritogames.generator;

import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.Generator;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.shared.logger.Logger;

import java.util.ArrayList;
import java.util.Random;

public class ASDGenerator implements Generator {
    public static final int MIN_WORLD_SIZE = 17;
    public static final int MAX_WORLD_SIZE = 69;

    private final GridGenerator gridGenerator;
    private final EntityGenerator entityGenerator;

    public ASDGenerator() {
        gridGenerator = new GridGenerator();
        entityGenerator = new EntityGenerator();
    }

    public ASDGenerator(GridGenerator gridGenerator, EntityGenerator entityGenerator) {
        this.gridGenerator = gridGenerator;
        this.entityGenerator = entityGenerator;
    }

    @Override
    public World generateWorld(long seed, int tilesPerPlayer, int difficulty, ArrayList<Character> characters) throws AbsentEntityException {
        Logger.logMethodCall(this);
        WorldBench bench = new WorldBench(calculateWorldSize(characters.size(), tilesPerPlayer));
        bench = gridGenerator.generateGrid(bench, seed);
        bench = entityGenerator.generateEntities(bench, seed, difficulty, characters);
        return bench.toWorld();
    }

    @Override
    public long generateSeed() {
        Logger.logMethodCall(this);
        if(Logger.isActive()) return 1;
        return new Random().nextLong();
    }

    public int calculateWorldSize(int amountOfPlayers, int tilesPerPlayer) {
        int worldSize = (int) Math.round(Math.sqrt((amountOfPlayers * tilesPerPlayer) / 0.45) * 0.995);
        return (worldSize <= MIN_WORLD_SIZE) ?
                MIN_WORLD_SIZE :
                Math.min(worldSize, MAX_WORLD_SIZE);
    }
}
