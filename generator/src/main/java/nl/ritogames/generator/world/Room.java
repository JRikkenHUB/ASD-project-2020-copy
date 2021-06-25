package nl.ritogames.generator.world;

import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final int x;
    private final int y;
    public final int width;
    public final int height;
    public final float priority;
    public final List<WorldTile> tiles = new ArrayList<>();

    public Room(int x, int y, int width, int height, float priority) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.priority = priority;
    }

    public void placeRoom(WorldBench bench) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int tileX = Math.toIntExact((long) (i - Math.floor(width / 2.0) + this.x));
                int tileY = Math.toIntExact((long) (j - Math.floor(height / 2.0) + this.y));

                if (bench.isValidCoordinate(tileX, tileY)) {
                    WorldTile worldTile = new AccessibleWorldTile(tileX, tileY);
                    tiles.add(worldTile);
                    bench.setTile(worldTile);
                }
            }
        }
    }

    public List<WorldTile> getTiles() {
        return tiles;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
