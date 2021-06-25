package nl.ritogames.trui.uicomponent.screen;

import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import nl.ritogames.trui.uicomponent.Vector;

/**
 * <p>Is a base for all implementations of itself {@link AsciiRenderedScreen}</p>
 */
public abstract class AsciiRenderedScreen implements Screen {

  protected IContextBuilder contextBuilder;
  protected IRender render;

  protected void resetRenderer() {
    Vector screenSize = new Vector(80, 22);
    this.render = new Render();
    this.contextBuilder = this.render.newBuilder();
    contextBuilder.width(screenSize.getX());
    contextBuilder.height(screenSize.getY());
  }

}
