package nl.ritogames.shared.dto.gameobject.attribute;

import nl.ritogames.shared.dto.gameobject.individual.Character;

/**
 * The attribute type weapon.
 */
public class Weapon extends Attribute {
    private int attBuff;

    /**
     * The Weapon attribute is used to increase the attack damage of a character
     * @param name
     * @param attBuff
     */
    public Weapon(String name, int attBuff) {
        super(name);
        this.attBuff = attBuff;
    }

    public Weapon() {
        super("default");
        attBuff = 5;
        //standard constructor
    }


    @Override
    public void apply(Character character) {
        character.increaseAttack(attBuff);
    }

    public int getAttBuff() {
        return attBuff;
    }

    public void setAttBuff(int attBuff) {
        this.attBuff = attBuff;
    }
}
