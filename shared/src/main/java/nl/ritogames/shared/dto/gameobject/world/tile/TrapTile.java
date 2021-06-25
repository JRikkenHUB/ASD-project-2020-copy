package nl.ritogames.shared.dto.gameobject.world.tile;

import nl.ritogames.shared.dto.gameobject.trap.Trap;

/**
 * Tile which is accessible and is a trap.
 */
public class TrapTile extends AccessibleWorldTile {

    private Trap trap;

    /**
     * Creates a TrapTile and puts a Trap on it
     * @param x
     * @param y
     * @param trap
     */
    public TrapTile(int x, int y, Trap trap) {
        super(x, y);
        this.trap = trap;
    }

    public TrapTile() {
    }

    public Trap getTrap() {
        return trap;
    }

    public void setTrap(Trap trap) {
        this.trap = trap;
    }
}
