package nl.ritogames.generator.entity;

import com.raylabz.opensimplex.OpenSimplexNoise;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import nl.ritogames.generator.world.Room;
import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.TrapTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.exception.AbsentEntityException;

/**
 * The PlayerBuilder is responsible for placing the players in the world
 */
public class PlayerBuilder {
    protected Random random;
    protected OpenSimplexNoise noise;

    /**
     * Continues building the WorldBench by adding players to it.
     *
     * @param bench                     The WorldBench that contains the current world.
     * @param characters                The list of characters that need to be added.
     * @param seed                      The seed of the the seed with which the world was generated.
     * @return                          A new WorldBench with the players on it.
     * @throws AbsentEntityException    Thrown when the player that is spawned is null.
     */
    public WorldBench build(WorldBench bench, long seed, ArrayList<Character> characters) throws AbsentEntityException {
        random = new Random(seed);
        noise = new OpenSimplexNoise(seed);
        double spawnFactor = 0.5;
        noise.setFeatureSize(1);
        spawnPlayers(bench, characters, spawnFactor);
        return bench;
    }
    /**
     * This method spawns the players
     *
     * @param characters                The list of characters that need to be added.
     * @param bench                     The WorldBench that contains the current world.
     * @throws AbsentEntityException    Thrown when the player that is spawned is null.
     */
    private void spawnPlayers(WorldBench bench, ArrayList<Character> characters, double spawnFactor) throws AbsentEntityException {
        int playersSpawned = 0;
        NavigableMap<Double, AccessibleWorldTile> reversed = new TreeMap<>(getPossibleTiles(bench, spawnFactor)).descendingMap();
        for (Map.Entry<Double, AccessibleWorldTile> entry : reversed.entrySet()) {
            if (playersSpawned >= characters.size()) {
                continue;
            }
            spawnEntity(entry.getValue(), characters.get(playersSpawned));
            playersSpawned++;
        }
    }

    /**
     * This method places the players in a room
     *
     * @param characters The list of characters that need to be added
     * @param seed       The seed with which the world was generated
     * @param rooms      A list of rooms that have no present characters
     */
    public void setPlayersInRoom(ArrayList<Character> characters, long seed, List<Room> rooms) {
        Collections.shuffle(rooms, new Random(seed));
        int roomIndex = 0;
        for (int i = 0; i < characters.size(); i++) {
            roomIndex++;
            if (roomIndex == rooms.size()) roomIndex = 0;
            characters.get(i).setLocation(
                    new ASDVector(
                            rooms.get(i).getX(),
                            rooms.get(i).getY())
            );
        }
    }

    /**
     * Get all the accesibleWorldTiles in a given world
     *
     * @param bench         The world of which to get the tiles.
     * @param spawnFactor   The spawnfactor of the tiles.
     * @return              Hashmap of all the accesibleWorldTiles.
     */
    private HashMap<Double, AccessibleWorldTile> getPossibleTiles(WorldBench bench, double spawnFactor) {
        HashMap<Double, AccessibleWorldTile> possibleTiles = new HashMap<>();
        for (WorldTile[] tiles : bench.getTiles()) {
            for (WorldTile tile : tiles) {
                double noiseValue = noise.getNoise2D(tile.getCoordinates().getX(), tile.getCoordinates().getY()).getValue();
                if (noiseValue >= spawnFactor && tileIsSuited(tile)) {
                    possibleTiles.put(noiseValue, (AccessibleWorldTile) tile);
                }
            }
        }
        return possibleTiles;
    }

    /**
     * Checks if a tile is an AccessibleWorldTile, but not a Traptile.
     *
     * @param tile The tile to check.
     * @return If the tile is suited.
     */
    protected boolean tileIsSuited(WorldTile tile) {
        return (tile instanceof AccessibleWorldTile) && !(tile instanceof TrapTile) && (tile
            .tileAccessible());
    }

    /**
     * Spawns a player on the given tile.
     *
     * @param tile                      The tile on which to spawn the character.
     * @param character                 The character to spawn.
     * @throws AbsentEntityException    Thrown when the character doesn't exist.
     */
    private void spawnEntity(AccessibleWorldTile tile, Character character) throws AbsentEntityException {
        tile.addIndividual(character);
        character.setLocation(tile.getCoordinates());
    }
}
