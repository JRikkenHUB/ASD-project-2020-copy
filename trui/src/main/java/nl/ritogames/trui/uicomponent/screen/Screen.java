package nl.ritogames.trui.uicomponent.screen;

import nl.ritogames.trui.render.UIContext;

/**
 * Functional interface for the different implementations of screen
 */
public interface Screen {

  /**
   * <p>Draws the context given to the command line interface</p>
   *
   * @param context the context of the current draw iteration
   */
  void draw(UIContext context);
}
