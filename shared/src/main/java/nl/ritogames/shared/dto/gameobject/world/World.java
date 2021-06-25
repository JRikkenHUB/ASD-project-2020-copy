package nl.ritogames.shared.dto.gameobject.world;

import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.world.tile.ASDTile;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.InaccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;
import nl.ritogames.shared.exception.OutOfMapException;

/**
 * The type World which represents the World in a 2D Array form.
 */
public class World {

    private WorldTile[][] tiles;

    /**
     * Instantiates a new World with regular WorldTiles.
     *
     * @param tiles the WorldTiles
     */
    public World(WorldTile[][] tiles) {
        this.tiles = tiles;
    }

    public World() {

    }

    /**
     * Separate Constructor to convert a Viewable World to a real World Instantiates a new World from
     * a viewable Map.
     *
     * @param mapTiles the ASDTiles from the Map
     * @throws OutOfMapException Exception in case the view is not from inside the world.
     */
    public World(ASDTile[][] mapTiles) throws OutOfMapException {
        ASDTile centerTile = mapTiles[mapTiles.length / 2][mapTiles[0].length / 2];
        if (!(centerTile instanceof WorldTile)) {
            throw new OutOfMapException("Character seems to from out of this world");
        }
        ASDVector centerPoint = ((WorldTile) centerTile).getCoordinates();
        int mapSize = Math
            .max((centerPoint.getX() + mapTiles[0].length), (centerPoint.getY() + mapTiles.length));
        this.tiles = new WorldTile[mapSize][mapSize];
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                this.tiles[x][y] = new InaccessibleWorldTile(x, y);
            }
        }
        for (int x = 0; x < mapTiles[0].length; x++) {
            for (int y = 0; y < mapTiles.length; y++) {
                if (mapTiles[x][y] instanceof AccessibleWorldTile) {
                    ASDVector tileVector = ((WorldTile) mapTiles[x][y]).getCoordinates();
                    this.tiles[tileVector.getX()][tileVector.getY()] = (WorldTile) mapTiles[x][y];
                }
            }
        }

    }

    /**
     * Gets the Tile on the given vector
     *
     * @param location location of the tile
     * @return the tile
     */
    public WorldTile getTile(ASDVector location) {
        return getTile(location.getX(), location.getY());
    }

    /**
     * Gets the Tile on the given Coordinates
     *
     * @param x the x
     * @param y the y
     * @return the tile
     */
    public WorldTile getTile(int x, int y) {
        if (!checkOutOfMap(x, y)) {
            return tiles[x][y];
        } else {
            return new InaccessibleWorldTile(x, y);
        }
    }

    /**
     * Get World in a 2D Array format.
     *
     * @return the World as WorldTiles[][]
     */
    public WorldTile[][] getTiles() {
        return tiles;
    }

    /**
     * Get the size of the World.
     *
     * @return the world size
     */
    public int calcWorldSize() {
        return tiles.length;
    }

    /**
     * Check if coordinates are outside of the Map.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return a boolean
     */
    public boolean checkOutOfMap(int x, int y) {
        return x < 0 || y < 0 || calcWorldSize() <= y || calcWorldSize() <= x;
    }

    public void setWorldSize(int worldSize) {
        //Required for the JSON Converer
    }

    public void setTiles(WorldTile[][] tiles) {
        this.tiles = tiles;
    }
}
