package nl.ritogames.shared.dto.command.host;

import nl.ritogames.shared.GameStateModifier;
import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.exception.CommandFailedException;

public class ChangeHostCommand implements Command {
    private String hostId;

    public ChangeHostCommand(String hostId) {
        this.hostId = hostId;
    }

    @Override
    public void execute(GameStateModifier gameStateModifier) throws CommandFailedException {
        gameStateModifier.getGameState().setHostIndividualId(hostId);
    }

    public ChangeHostCommand() {
        super();
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}
