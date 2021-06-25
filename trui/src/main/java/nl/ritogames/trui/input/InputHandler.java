package nl.ritogames.trui.input;

/**
 * <p>Handles all the input given by the user</p>
 */
public class InputHandler {


  private final IScanner scanner;
  private String input;

  public InputHandler(IScanner scanner) {
    input = "";
    this.scanner = scanner;
  }

  /**
   * <p>Request the {@link IScanner} to get the input, if there is no input {@link #input} will
   * contain null</p>
   */
  public void requestInput() {
    this.input = scanner.nextLine().strip();
  }

  /**
   * @return <p>true when empty</p>
   */
  public boolean isEmpty() {
    return this.input.isEmpty();
  }

  /**
   * @return <p>The input given by the user</p>
   */
  public String peek() {
    return this.input;
  }

  /**
   * <p>Resets the input after usage</p>
   *
   * @return <p>The input given by the user</p>
   */
  public String removeAndFetchInput() {
    String temp = input;
    input = "";
    return temp;
  }
}
