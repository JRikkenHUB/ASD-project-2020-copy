package nl.ritogames.shared.dto.gameobject.individual;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.ritogames.shared.dto.gameobject.ASDVector;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@individualType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Monster.class, name = "Monster"),
        @JsonSubTypes.Type(value = Character.class, name = "Character"),
})
/**
 * Abstract class for the individuals in the game.
 */
public abstract class Individual {

    protected ASDVector location;
    protected int maxHp;
    protected int attack;
    protected int defense;

    private int hp;

    private String individualID;
    protected String agentJson;

    protected Individual() {
    }

    protected Individual(ASDVector location, int maxHp, int attack, int defense) {
        this.maxHp = maxHp;
        this.attack = attack;
        this.hp = maxHp;
        this.defense = defense;
        this.location = location;
    }
    protected Individual(int maxHp, int attack, int defense) {
        this(null, maxHp, attack, defense);
    }

    protected Individual(int x, int y, int maxHp, int attack, int defense) {
        this(new ASDVector(x, y), maxHp, attack, defense);
    }

    public ASDVector getLocation() {
        return location;
    }

    public void setLocation(ASDVector location) {
        this.location = location;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (this.hp + hp > maxHp) this.hp = maxHp;
        else this.hp = hp;
    }

    public String getIndividualID() {
        return individualID;
    }

    public void setIndividualID(String individualID) {
        this.individualID = individualID;
    }

    public int lowerHp(int hp) {
        return this.hp -= hp;
    }

    public String getAgentJson() {
        return agentJson;
    }

    public void setAgentJson(String agentJson) {
        this.agentJson = agentJson;
    }
}
