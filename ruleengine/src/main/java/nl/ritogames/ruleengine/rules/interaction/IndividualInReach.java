package nl.ritogames.ruleengine.rules.interaction;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.helpers.Helper;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.AttackEvent;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;

/**
 * A rule that checks if there is a individual on a target tile when attacking
 */
public class IndividualInReach implements Rule<AttackEvent> {
    /**
     * Check target
     *
     * @param event    an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return whether a individual is present on the target tile
     */
    @Override
    public boolean validate(AttackEvent event, GameStateModifier modifier) {
        WorldTile tile = Helper.getTileFromEvent(event, modifier);
        if (tile instanceof AccessibleWorldTile) {
            return ((AccessibleWorldTile) tile).getIndividual() != null;
        } else return false;
    }
}
