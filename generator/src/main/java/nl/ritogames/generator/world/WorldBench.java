package nl.ritogames.generator.world;

import nl.ritogames.shared.dto.gameobject.world.World;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;

import java.util.ArrayList;
import java.util.List;

public class WorldBench {
    private List<Room> rooms;
    private List<Path> paths;
    private WorldTile[][] tiles;
    private int worldSize;

    public WorldBench(int worldSize) {
        rooms = new ArrayList<>();
        paths = new ArrayList<>();
        this.worldSize = worldSize;
    }

    public void setTile(WorldTile tile) {
        int x = tile.getCoordinates().getX();
        int y = tile.getCoordinates().getY();
        tiles[x][y] = tile;
    }

    public WorldTile getTile(int x, int y) {
        return tiles[x][y];
    }

    public boolean isValidCoordinate(int x, int y) {
        return (x < tiles.length && y < tiles.length && x >= 0 && y >= 0);
    }

    public World toWorld() {
        return new World(tiles);
    }

    public WorldTile[][] getTiles() {
        return tiles;
    }

    public void setTiles(WorldTile[][] tiles) {
        this.tiles = tiles;
    }

    public int getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public void addPath(Path path) {
        paths.add(path);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
}
