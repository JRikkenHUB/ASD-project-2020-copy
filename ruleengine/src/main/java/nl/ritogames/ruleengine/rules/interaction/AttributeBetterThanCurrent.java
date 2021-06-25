package nl.ritogames.ruleengine.rules.interaction;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.helpers.Helper;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.PickUpEvent;
import nl.ritogames.shared.dto.gameobject.attribute.Armour;
import nl.ritogames.shared.dto.gameobject.attribute.Attribute;
import nl.ritogames.shared.dto.gameobject.attribute.Potion;
import nl.ritogames.shared.dto.gameobject.attribute.Weapon;
import nl.ritogames.shared.dto.gameobject.individual.Character;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;

public class AttributeBetterThanCurrent implements Rule<PickUpEvent> {

    /**
     * Checks if the attribute that an individual want to pickup is better than the current attribute of the player.
     *
     * @param event     An instance of PickUpEvent.
     * @param modifier  Contains the GameStateModifier so data from the GameState can be fetched.
     * @return          a boolean whether the attribute is better.
     */
    @Override
    public boolean validate(PickUpEvent event, GameStateModifier modifier) {
        Character individual = (Character) modifier.getGameState().getIndividual(event.getIndividualId());
        WorldTile tile = Helper.getTileFromEvent(event, modifier);
        if (tile instanceof AccessibleWorldTile && ((AccessibleWorldTile) tile).hasAttribute()) {
            Attribute attribute = ((AccessibleWorldTile)tile).getAttribute();
            if (attribute instanceof Weapon) {
                return (((Weapon) attribute).getAttBuff() >= individual.getAttack()-Character.CHARACTER_BASE_ATTACK);
            } else if(attribute instanceof Armour) {
                return (((Armour) attribute).getDefBuff() >= individual.getDefense()-Character.CHARACTER_BASE_DEFENSE);
            } else return attribute instanceof Potion;
        }
        return false;
    }
}
