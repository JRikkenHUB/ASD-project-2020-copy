package nl.ritogames.ruleengine.rules.winning.winconditions;

import nl.ritogames.shared.GameStateModifier;

/**
 * An abstract class that can specify winning conditions
 */
public abstract class WinCondition {
    protected GameStateModifier modifier;

    protected WinCondition(GameStateModifier modifier) {
        this.modifier = modifier;
    }

    public abstract boolean isMet();

    public void setModifier(GameStateModifier modifier) {
        this.modifier = modifier;
    }
}
