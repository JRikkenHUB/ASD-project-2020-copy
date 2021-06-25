package nl.ritogames.ruleengine.rules.interaction;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.helpers.Helper;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.PickUpEvent;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;


/**
 * A validation technique for a PickUpEvent to check if the location contains an attribute.
 */
public class AttributeInReach implements Rule<PickUpEvent> {
    /**
     * Validate if the move to the tile is accessible
     *
     * @param event    PickUpEvent an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return whether the attribute is valid
     */
    @Override
    public boolean validate(PickUpEvent event, GameStateModifier modifier) {
        WorldTile tile = Helper.getTileFromEvent(event, modifier);
        if (tile instanceof AccessibleWorldTile) {
            return ((AccessibleWorldTile) tile).getAttribute() != null;
        } else return false;
    }
}
