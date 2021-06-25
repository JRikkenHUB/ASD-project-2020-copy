package nl.ritogames;

import nl.ritogames.shared.CommandHandler;
import nl.ritogames.shared.CommandSender;
import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.dto.event.InteractionEvent;
import nl.ritogames.shared.exception.CommandNotGeneratedException;
import nl.ritogames.shared.exception.EventFailedException;
import nl.ritogames.shared.logger.Logger;

import javax.inject.Inject;

public class ASDCommandHandler implements CommandHandler {
    private CommandSender commandSender;

    /**
     * Accepts an event, tries to convert it to a command and commits the command.
     *
     * @param event The event to process.
     */
    @Override
    public void handleEventIntoCommand(InteractionEvent event) throws EventFailedException {
        Logger.logMethodCall(this);
        try {
            Command command = CommandFactory.produceCommand(event);
            commitCommand(command);
        } catch (CommandNotGeneratedException e) {
            throw new EventFailedException(e);
        }
    }

    /**
     * Accepts an event and sends it to de commandSender.
     *
     * @param command The command to commit.
     */
    private void commitCommand(Command command) {
        Logger.logMethodCall(this);
        commandSender.sendCommand(command);
    }

    @Inject
    public void setCommandSender(CommandSender commandSender) {
        this.commandSender = commandSender;
    }
}
