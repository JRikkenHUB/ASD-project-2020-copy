package nl.ritogames.trui.uicomponent;

import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.Rectangle;
import nl.ritogames.trui.render.UIContext;

import java.util.Objects;

/**
 * The command line interface
 */
public class UIComponent {

  protected IRender render;
  protected IContextBuilder builder;

  private final Vector position;
  private final Vector size;

  public UIComponent(Vector position, Vector size) {
    this.position = position;
    this.size = size;
  }

  /**
   * <p>This methode is responsible for drawing this component onto the screen.
   * Only use this methode for draw related activities</p>
   */
  public ICanvas draw(UIContext uiContext) {
    this.render = new Render();
    this.builder = render.newBuilder().width(size.getX()).height(size.getY());
    this.builder.element(new Rectangle());
    return this.render.render(this.builder.build());
  }

  public Vector getPosition() {
    return position;
  }

  public Vector getSize() {
    return size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UIComponent that = (UIComponent) o;
    return Objects.equals(position, that.position) &&
        Objects.equals(size, that.size);
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, size);
  }

  @Override
  public String toString() {
    return "UIComponent{" +
        "position=" + position +
        ", size=" + size +
        '}';
  }
}
