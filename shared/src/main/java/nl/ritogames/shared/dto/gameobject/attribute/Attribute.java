package nl.ritogames.shared.dto.gameobject.attribute;

import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.individual.Character;

/**
 * Abstract class for the attributes in the game.
 */
public abstract class Attribute {
    protected String name;
    private String attributeID;
    private ASDVector location;

    protected Attribute(){}

    protected Attribute(String name){
        this.name = name;
    }

    protected Attribute(String name, String attributeID, ASDVector location) {
        this.name = name;
        this.attributeID = attributeID;
        this.location = location;
    }

    /**
     * <p>This method applies an effect on the given character.
     * This way new attributes can always be added.
     * The character should always pick up the attribute and will apply the method to itself.</p>
     *
     * @param character the character to apply to
     */
    public abstract void apply(final Character character);

    public String getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(String attributeID) {
        this.attributeID = attributeID;
    }

    public ASDVector getLocation() {
        return location;
    }

    public void setLocation(ASDVector location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

