package nl.ritogames.trui;

import nl.ritogames.shared.dto.GameStateContext;
import nl.ritogames.shared.dto.gameobject.individual.Individual;
import nl.ritogames.shared.exception.AbsentEntityException;
import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.uicomponent.Vector;
import nl.ritogames.trui.uicomponent.stats.Stats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StatsTest {

  private Stats sut;
  private UIContext uiContext;
  private GameStateContext gameStateContext;
  private Individual self;

  @BeforeEach
  void setUp() throws AbsentEntityException {
    sut = new Stats(Vector.ZERO, Fixtures.testScreenSize());
    uiContext = Mockito.mock(UIContext.class);
    gameStateContext = Mockito.mock(GameStateContext.class);
    self = Mockito.mock(Individual.class);
    Mockito.when(uiContext.getGameStateContext()).thenReturn(gameStateContext);
    Mockito.when(gameStateContext.getSelf()).thenReturn(self);
    Mockito.when(self.getHp()).thenReturn(100);
  }

  @Test
  void draw() {
    String expected = sut.draw(uiContext).getText();
    Assertions.assertEquals(expected, Fixtures.getDrawnStats());
  }
}
