package nl.ritogames.shared;

import nl.ritogames.shared.dto.command.Command;

public interface CommandSender {
    void sendCommand(Command command);
}
