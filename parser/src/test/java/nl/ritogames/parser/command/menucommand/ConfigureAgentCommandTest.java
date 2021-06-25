package nl.ritogames.parser.command.menucommand;

import nl.ritogames.shared.dto.event.AgentConfigureEvent;
import nl.ritogames.shared.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigureAgentCommandTest {

  private ConfigureAgentCommand sut;

  @BeforeEach
  void setUp() {
    sut = new ConfigureAgentCommand();
  }

  @Test
  void toInteractionEvent() throws InvalidArgumentException {
    var actual = sut.toInteractionEvent("");
    assertTrue(actual instanceof AgentConfigureEvent);
  }
}
