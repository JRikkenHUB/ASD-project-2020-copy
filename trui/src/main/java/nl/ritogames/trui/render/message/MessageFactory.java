package nl.ritogames.trui.render.message;

import nl.ritogames.shared.dto.command.Command;
import nl.ritogames.shared.dto.command.individual.*;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MessageFactory {

  public static final String SENDER_GAME = "Game";
  private final Map<Class<? extends Command>, MessageFormatter> messageFormatterMap;

  public MessageFactory() {
    this.messageFormatterMap = new HashMap<>();
    this.addMessageFormatter(MoveCommand.class, moveCommand -> String
        .format("Character %s moved %s", moveCommand.getInitiatingIndividualId(),
            moveCommand.getDirection().toString().toLowerCase()));
    this.addMessageFormatter(
        AttackCommand.class, attackCommand -> String.format("Character %s attacked Character %s!",
            attackCommand.getInteractingIndividualId(),
            attackCommand.getInteractingIndividualId()));
    this.addMessageFormatter(PickUpAttributeCommand.class, pickupCommand -> String
        .format("Character %s picked up an item!", pickupCommand.getInitiatingIndividualId()));
    this.addMessageFormatter(DamageIndividualCommand.class, damageCommand -> String
        .format("Character %s dealt a damaging blow directing %s!", damageCommand.getInitiatingIndividualId(), damageCommand.getDirection().toString().toLowerCase()));
    this.addMessageFormatter(WinCommand.class, winCommand -> "You have won the game!");
    this.addMessageFormatter(DeathCommand.class, deathCommand -> String.format("%s died! Good luck to the rest of you!", deathCommand.getInitiatingIndividualId()));
  }

  private static String defaultCommandMessage(Command cmd) {
    return cmd.getClass().getSimpleName();
  }

  public Message fromCommand(Command command) {
    String content = messageFormatterMap
        .getOrDefault(command.getClass(), MessageFactory::defaultCommandMessage)
        .toMessage(command);
    return new Message(content, SENDER_GAME);
  }

  public <T extends Command> void addMessageFormatter(Class<T> command,
      MessageFormatter<T> formatter) {
    this.messageFormatterMap.put(command, formatter);
  }
}
