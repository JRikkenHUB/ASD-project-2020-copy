package nl.ritogames.trui.input;

/**
 * Functional interface for the {@link ScannerImpl}
 */
public interface IScanner {

  /**
   * @return <p>The input given by the user</p>
   */
  String nextLine();
}
