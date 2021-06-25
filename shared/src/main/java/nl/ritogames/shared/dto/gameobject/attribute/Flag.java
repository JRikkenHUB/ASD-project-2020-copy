package nl.ritogames.shared.dto.gameobject.attribute;

import nl.ritogames.shared.dto.gameobject.individual.Character;

/**
 * The attribute type flag.
 */
public class Flag extends Attribute {

    public Flag() {
        //Standard constructor
    }

    @Override
    public void apply(Character character) {
        character.setHasFlag(true);
    }
}
