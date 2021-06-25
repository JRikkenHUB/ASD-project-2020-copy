package nl.ritogames.intelligentagent.behavior;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Predicate;
import nl.ritogames.shared.dto.GameStateContext;
import org.junit.jupiter.api.Test;

class DecisionNodeTest {

  private final GameStateContext contextMock = mock(GameStateContext.class);
  Predicate<GameStateContext> condition = mock(Predicate.class);
  BehaviorNode ifTrue = mock(BehaviorNode.class);
  BehaviorNode ifFalse = mock(BehaviorNode.class);
  ActionNode nextNode = mock(ActionNode.class);
  private DecisionNode sut;

  @Test
  void executeShouldCallIfTrueAndNextNodeIfConditionIsTrue() {
    when(condition.test(contextMock)).thenReturn(true);
    sut = new DecisionNode(condition, ifTrue, ifFalse, nextNode);

    sut.execute(contextMock);

    verify(ifTrue).execute(contextMock);
    verify(ifFalse, times(0)).execute(any(GameStateContext.class));
    verify(nextNode).execute(contextMock);
  }

  @Test
  void executeShouldCallIfFalseAndNextNodeIfConditionIsFalse() {
    when(condition.test(contextMock)).thenReturn(false);
    sut = new DecisionNode(condition, ifTrue, ifFalse, nextNode);

    sut.execute(contextMock);

    verify(ifFalse).execute(contextMock);
    verify(ifTrue, times(0)).execute(any(GameStateContext.class));
    verify(nextNode).execute(contextMock);
  }
}
