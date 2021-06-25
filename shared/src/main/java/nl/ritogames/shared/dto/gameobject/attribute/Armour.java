package nl.ritogames.shared.dto.gameobject.attribute;

import nl.ritogames.shared.dto.gameobject.individual.Character;

public class Armour extends Attribute {
    private int defBuff;

    /**
     * Armor attribute is used to increase the defense of character
     *
     * @param name
     * @param defBuff
     */
    public Armour(String name, int defBuff) {
        super(name);
        this.defBuff = defBuff;
    }

    public Armour() {
        super("default");
        defBuff = 5;
        //Standard constructor
    }

    @Override
    public void apply(Character character) {
        character.increaseDefense(defBuff);
    }

    public int getDefBuff() {
        return defBuff;
    }

    public void setDefBuff(int defBuff) {
        this.defBuff = defBuff;
    }
}
