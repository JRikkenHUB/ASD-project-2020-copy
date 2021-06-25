package nl.ritogames;

import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.dto.command.host.ChangeHostCommand;
import nl.ritogames.shared.dto.command.individual.*;
import nl.ritogames.shared.dto.command.menu.StartGameCommand;
import nl.ritogames.shared.dto.event.*;
import nl.ritogames.shared.exception.CommandNotGeneratedException;

public class CommandFactory {
    private CommandFactory() {
    }
    /**
     * Produces the command needed to process the given event.
     *
     * @param event The event to process.
     * @return Returns the produced command.
     * @throws CommandNotGeneratedException Thrown when an unknown event needs to be processed and a command cannot be produced.
     */
    public static Command produceCommand(InteractionEvent event) throws CommandNotGeneratedException {
        Command command;
        if (event instanceof MoveEvent) {
            command = new MoveCommand(event.getIndividualId(), ((MoveEvent) event).getDirection());
        } else if (event instanceof PickUpEvent) {
            command = new PickUpAttributeCommand(event.getIndividualId(), ((PickUpEvent) event).getDirection());
        } else if (event instanceof CreateGameEvent) {
            command = new CreateGameCommand(event.getGameName(), event.getIndividualId(), ((CreateGameEvent) event).getHostAgentName());
        } else if (event instanceof GameJoinEvent) {
            command = new JoinGameCommand(event.getIndividualId());
        } else if (event instanceof StartGameEvent) {
            command = new StartGameCommand(((StartGameEvent) event).getTilesPerPlayer(), ((StartGameEvent) event).getDifficulty(), ((StartGameEvent) event).getGameState());
        } else if (event instanceof StartAgentEvent) {
            command = new StartAgentCommand(event.getIndividualId());
        } else if (event instanceof StopAgentEvent) {
            command = new StopAgentCommand(event.getIndividualId());
        } else if (event instanceof StopGameEvent) {
            command = new StopGameCommand(event.getIndividualId());
        } else if (event instanceof WinConditionMetEvent) {
            command = new WinCommand(event.getIndividualId());
        } else if (event instanceof AttackEvent) {
            command = new DamageIndividualCommand(event.getIndividualId(), ((AttackEvent) event).getDirection());
        } else if (event instanceof ChangeHostEvent) {
            command = new ChangeHostCommand(event.getIndividualId());
        } else {
            throw new CommandNotGeneratedException();
        }
        return command;
    }
}
