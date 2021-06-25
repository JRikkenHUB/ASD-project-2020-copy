package nl.ritogames.ruleengine.rules.movement;

import nl.ritogames.ruleengine.rules.Rule;
import nl.ritogames.ruleengine.rules.helpers.Helper;
import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.event.MoveEvent;
import nl.ritogames.shared.dto.gameobject.world.tile.AccessibleWorldTile;
import nl.ritogames.shared.dto.gameobject.world.tile.WorldTile;

/**
 * A validation technique for a MoveEvent to check if the location is accessible or not
 */
public class MoveEventLocationInaccessible implements Rule<MoveEvent> {
    /**
     * Validate if the move to the tile is accessible
     *
     * @param event    MoveEvent an instance of InteractionEvent
     * @param modifier contains the GameStateModifier so data from the GameState can be fetched
     * @return whether the tile to move to is valid
     */
    @Override
    public boolean validate(MoveEvent event, GameStateModifier modifier) {
        try {
            WorldTile tile = Helper.getTileFromEvent(event, modifier);
            return tile instanceof AccessibleWorldTile;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
}