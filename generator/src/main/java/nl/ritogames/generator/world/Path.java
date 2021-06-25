package nl.ritogames.generator.world;

import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;

import java.util.Random;

public class Path {
    private final WorldTile aConnection;
    private final WorldTile bConnection;

    public Path(Room roomA, Room roomB) {
        this.aConnection = roomA.tiles.get(Math.round(new Random((long) ((roomA.getX() + roomA.getY() + roomA.priority) * 100)).nextFloat()));
        this.bConnection = roomB.tiles.get(Math.round(new Random((long) ((roomB.getX() + roomB.getY() + roomB.priority) * 100)).nextFloat()));
    }

    public void placePath(WorldBench bench) {
        ASDVector currentLocation = aConnection.getCoordinates();
        ASDVector targetLocation = bConnection.getCoordinates();

        while (!(currentLocation.equals(targetLocation))) {
            walk(currentLocation, targetLocation, bench);
        }
    }

    private void walk(ASDVector currentLocation, ASDVector targetLocation, WorldBench bench) {
        Random random = new Random((currentLocation.getX() + currentLocation.getX()) * 100L);

        int xDifference = targetLocation.getX() - currentLocation.getX();
        int yDifference = targetLocation.getY() - currentLocation.getY();

        ASDVector walkDirection = getWalkDirection(xDifference, yDifference, random);

        addVectors(currentLocation, walkDirection);
        bench.setTile(new AccessibleWorldTile(currentLocation.getX(), currentLocation.getY()));
    }

    private ASDVector getWalkDirection(int xDifference, int yDifference, Random random) {
        ASDVector direction = new ASDVector();

        if (xDifference < 0) direction.setX(-1);
        else if (xDifference > 0) direction.setX(1);

        if (yDifference < 0) direction.setY(-1);
        else if (yDifference > 0) direction.setY(1);

        if (yDifference == 0 || xDifference == 0) return direction;

        if (random.nextFloat() > 0.5) direction.setX(0);
        else direction.setY(0);

        return direction;
    }

    private void addVectors(ASDVector vector1, ASDVector vector2) {
        vector1.setX(vector1.getX() + vector2.getX());
        vector1.setY(vector1.getY() + vector2.getY());
    }
}
