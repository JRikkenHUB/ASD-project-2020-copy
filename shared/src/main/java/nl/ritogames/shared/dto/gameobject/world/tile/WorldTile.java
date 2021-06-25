package nl.ritogames.shared.dto.gameobject.world.tile;

import nl.ritogames.shared.dto.gameobject.ASDVector;

/**
 * The worldtile type asdtile.
 */
public abstract class WorldTile implements ASDTile {

    protected ASDVector coordinates;

    protected WorldTile() {

    }

    protected WorldTile(ASDVector coordinates) {
        this.coordinates = coordinates;
    }

    protected WorldTile(int x, int y) {
        coordinates = new ASDVector(x, y);
    }

    public ASDVector getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ASDVector coordinates) {
        this.coordinates = coordinates;
    }

    public abstract boolean hasIndividual();

    public abstract boolean tileAccessible();
}
