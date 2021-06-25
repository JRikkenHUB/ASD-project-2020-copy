package nl.ritogames.trui.uicomponent.screen;

import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContext;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import nl.ritogames.trui.render.UIContext;
import nl.ritogames.trui.uicomponent.UIComponent;
import nl.ritogames.trui.uicomponent.Vector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameScreenTest {

  private GameScreen sut;
  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private UIComponent uiComponent;
  private IRender render;


  @BeforeEach
  void setUp() {
    sut = new GameScreen();
    sut = Mockito.spy(sut);
    sut.uiComponents.clear();
    uiComponent = Mockito.mock(UIComponent.class);
    Vector vector = Mockito.mock(Vector.class);
    Mockito.when(vector.getX()).thenReturn(1);
    Mockito.when(vector.getY()).thenReturn(2);
    Mockito.when(uiComponent.getPosition()).thenReturn(vector);
    Mockito.when(uiComponent.getSize()).thenReturn(vector);
    Mockito.doNothing().when(sut).resetRenderer();
    render = Mockito.mock(IRender.class);
    sut.render = render;
    sut.contextBuilder = Mockito.mock(IContextBuilder.class);
    Mockito.when(sut.contextBuilder.build()).thenReturn(Mockito.mock(IContext.class));
    sut.uiComponents.add(uiComponent);
    System.setOut(new PrintStream(outputStreamCaptor));
    var canvas = Mockito.mock(ICanvas.class);
    Mockito.when(canvas.getText()).thenReturn("test");
    Mockito.when(render.render(Mockito.any(IContext.class))).thenReturn(canvas);
  }

  @Test
  void drawShouldPrint() {
    var context = Mockito.mock(UIContext.class);

    sut.draw(context);

    assertTrue(outputStreamCaptor.toString().trim().contains("test"));
  }

  @AfterEach
  void tearDown() {
    System.setOut(standardOut);
  }
}
