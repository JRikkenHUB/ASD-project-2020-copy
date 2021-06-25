package nl.ritogames.trui.exception;

/**
 * <p> Functional generic interface to handle all exceptions, should be inherited by all
 * implementations of an exception mapper</p>
 *
 * @param <T> <p>The exception that needs to be handled</p>
 */
public interface ExceptionMapper<T extends Exception> {

  void handle(T exception);
}
