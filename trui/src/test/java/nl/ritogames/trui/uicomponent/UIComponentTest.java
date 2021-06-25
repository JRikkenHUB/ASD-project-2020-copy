package nl.ritogames.trui.uicomponent;

import nl.ritogames.trui.Fixtures;
import nl.ritogames.trui.render.UIContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UIComponentTest {


  private UIComponent sut;
  private UIContext uiContextMock;

  @BeforeEach
  void setUp() {
    sut = new UIComponent(Vector.ZERO, Fixtures.testScreenSize());
    uiContextMock = Mockito.mock(UIContext.class);
  }

  @Test
  void TestIfDrawnCorrectly() {
    String expected = sut.draw(uiContextMock).getText();
    Assertions.assertEquals(expected, Fixtures.getDrawnUIComponent());
  }

}
