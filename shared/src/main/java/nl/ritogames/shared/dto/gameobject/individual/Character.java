package nl.ritogames.shared.dto.gameobject.individual;

import nl.ritogames.shared.dto.gameobject.ASDVector;
import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The individual type character.
 */
public class Character extends Individual {
    @JsonIgnore
    public static final int CHARACTER_BASE_MAX_HP = 100;
    @JsonIgnore
    public static final int CHARACTER_BASE_ATTACK = 20;
    @JsonIgnore
    public static final int CHARACTER_BASE_DEFENSE = 10;

    private boolean hasFlag = false;

    /**
     * The Character represents the players participating in a game. 
     * During a game players can control this character or let their intelligent agent take over.
     */
    public Character() {
        super(CHARACTER_BASE_MAX_HP, CHARACTER_BASE_ATTACK, CHARACTER_BASE_DEFENSE);
    }

    public Character(ASDVector location) {
        setMaxHp(CHARACTER_BASE_MAX_HP);
        setAttack(CHARACTER_BASE_ATTACK);
        setDefense(CHARACTER_BASE_DEFENSE);
        setLocation(location);
    }

    public Character(int x, int y) {
        setMaxHp(CHARACTER_BASE_MAX_HP);
        setAttack(CHARACTER_BASE_ATTACK);
        setDefense(CHARACTER_BASE_DEFENSE);
        setLocation(new ASDVector(x, y));
    }

    public boolean getHasFlag() {
        return hasFlag;
    }

    public void setHasFlag(boolean hasFlag) {
        this.hasFlag = hasFlag;
    }

    public void increaseDefense (int defenseBuff) {
        this.defense = CHARACTER_BASE_DEFENSE + defenseBuff;
    }

    public void increaseAttack(int attackBuff) {
        this.attack = CHARACTER_BASE_ATTACK + attackBuff;
    }

    public void increaseMaxHp(int healthBuff) {
        this.maxHp = CHARACTER_BASE_MAX_HP + healthBuff;
    }

    public void applyAttribute(Attribute attribute) {
        attribute.apply(this);
    }

}
