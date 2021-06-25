package nl.ritogames.shared.dto.gameobject.world.tile;

/**
 * Worldtile which is inaccessible.
 */
public class InaccessibleWorldTile extends WorldTile {
    public InaccessibleWorldTile(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean hasIndividual() {
        return false;
    }

    @Override
    public boolean tileAccessible() {
        return false;
    }

}
