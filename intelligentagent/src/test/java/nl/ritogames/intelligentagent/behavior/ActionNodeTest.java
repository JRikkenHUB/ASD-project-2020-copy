package nl.ritogames.intelligentagent.behavior;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;
import nl.ritogames.shared.dto.GameStateContext;
import org.junit.jupiter.api.Test;

class ActionNodeTest {

  private final GameStateContext contextMock = mock(GameStateContext.class);
  private ActionNode sut;

  @Test
  void executeShouldCallConsumerAndNextNode() {
    Consumer<GameStateContext> action = mock(Consumer.class);
    ActionNode nextNode = mock(ActionNode.class);
    sut = new ActionNode(nextNode, action);

    sut.execute(contextMock);

    verify(action).accept(contextMock);
    verify(nextNode).execute(contextMock);
  }
}
