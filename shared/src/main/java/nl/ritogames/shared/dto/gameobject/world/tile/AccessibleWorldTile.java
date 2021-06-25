package nl.ritogames.shared.dto.gameobject.world.tile;

import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.exception.AbsentEntityException;

/**
 * WorldTile which is accessible.
 */
public class AccessibleWorldTile extends WorldTile {
    private Individual individual;
    private Attribute attribute;

    public AccessibleWorldTile() {

    }

    public AccessibleWorldTile(int x, int y) {
        super(x, y);
    }

    public Individual getIndividual() {
        return individual;
    }

    public void addIndividual(Individual individual) throws AbsentEntityException {
        if (individual == null) throw new AbsentEntityException("individual cannot be null.");
        this.individual = individual;
    }

    public void removeIndividual() {
        individual = null;
    }

    @Override
    public boolean hasIndividual() {
        return individual != null;
    }

    @Override
    public boolean tileAccessible() {
        return !hasAttribute() && !hasIndividual();
    }

    public boolean hasAttribute() {
        return attribute != null;
    }

    /**
     * Removes the Attribute from the tile and returns it
     * @return
     * @throws AbsentEntityException
     */
    public Attribute pickUpAttribute() throws AbsentEntityException {
        if (attribute == null) throw new AbsentEntityException("No attribute on this tile.");
        Attribute pickUp = attribute;
        attribute = null;
        return pickUp;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * Places an Attribute on the Tile
     * @param attribute
     * @throws AbsentEntityException
     */
    public void addAttribute(Attribute attribute) throws AbsentEntityException {
        if (attribute == null) throw new AbsentEntityException("Attribute cannot be null.");
        this.attribute = attribute;
    }

    public void removeAttribute() {
        attribute = null;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return "AccessibleWorldTile{" +
                "individual=" + individual +
                ", attribute=" + attribute +
                ", coordinates=" + coordinates +
                '}';
    }
}
