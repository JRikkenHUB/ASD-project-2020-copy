package nl.ritogames.trui.render;

import nl.ritogames.shared.exception.ParseInputException;

/**
 * <p>Abstract class that holds the base functionality of each {@link RenderStrategy}
 * For an implementation example look at {@link OnCommandRenderStrategy}</p>
 */
public abstract class RenderStrategy {


  /**
   * @param individualId
   * @throws ParseInputException
   */
  public abstract void update(String individualId) throws ParseInputException;


  public abstract Strategy getStrategy();

  public abstract void displayToggleMessage(String individualId);

  public enum Strategy {REALTIME, ONCOMMAND}
}
