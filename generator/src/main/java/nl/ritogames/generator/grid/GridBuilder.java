package nl.ritogames.generator.grid;

import nl.ritogames.generator.world.WorldBench;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;

/**
 * The GridBuilder builds a grid of WorldTiles
 */
public class GridBuilder {
    /**
     * Builds a grid based on a WorldSize determined in the WorldBench
     *
     * @param worldBench the WorldBench that contains the world
     * @return the WorldBench with the updated grid of tiles
     */
    public WorldBench addGrid(WorldBench worldBench) {
        int worldSize = worldBench.getWorldSize();
        WorldTile[][] worldTiles = new WorldTile[worldSize][worldSize];
        for (int x = 0; x < worldSize; x++) {
            for (int y = 0; y < worldSize; y++) {
                worldTiles[x][y] = new InaccessibleWorldTile(x, y);
            }
        }
        worldBench.setTiles(worldTiles);
        return worldBench;
    }
}
