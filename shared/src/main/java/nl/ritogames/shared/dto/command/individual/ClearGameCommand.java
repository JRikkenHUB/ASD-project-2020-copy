package nl.ritogames.shared.dto.command.individual;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.command.Command;

public class ClearGameCommand implements Command {
    @Override
    public void execute(GameStateModifier gameStateModifier) {
        gameStateModifier.setGameState(null);
    }
}
