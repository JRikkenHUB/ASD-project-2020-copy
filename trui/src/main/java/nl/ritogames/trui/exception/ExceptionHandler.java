package nl.ritogames.trui.exception;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>When exceptions are thrown they will be added to exception mapper, and handle them correctly
 * by the implementation of
 * {@link ExceptionMapper} an example of this is {@link nl.ritogames.trui.exception.mappers.InvalidArgumentExceptionMapper}</p>
 */
public class ExceptionHandler {

  @SuppressWarnings("rawtypes")
  Map<Class<? extends Exception>, ExceptionMapper> exceptionMapperMap;

  public ExceptionHandler() {
    this.exceptionMapperMap = new HashMap<>();
  }

  /**
   * <p>Looks of there are any exceptions to be handled. If so it will use the {@link
   * ExceptionMapper#handle(Exception)} to handle the exception</p>
   *
   * @param e <p>The exception that needs to be handled</p>
   */
  public void handleException(Exception e) {
    if (exceptionMapperMap.containsKey(e.getClass())) {
      //noinspection unchecked
      exceptionMapperMap.get(e.getClass()).handle(e);
    } else {
      Logger.getAnonymousLogger().log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
    }
  }

  /**
   * <p>When an exception is thrown it will be added to the list of exceptions that need to be
   * handled {@link #exceptionMapperMap}</p>
   *
   * @param clazz           <p>The class of the Exception. So if an {@link nl.ritogames.shared.exception.InvalidArgumentException}
   *                        is thrown, the key InvalidArgumentException will be added to this
   *                        list</p>
   * @param exceptionMapper <p>The implementation of an {@link ExceptionMapper} for example {@link
   *                        nl.ritogames.trui.exception.mappers.InvalidArgumentExceptionMapper}</p>
   * @param <T>             <p>The exception</p>
   */
  public <T extends Exception> void addExceptionMapper(Class<T> clazz,
      ExceptionMapper<T> exceptionMapper) {
    this.exceptionMapperMap.put(clazz, exceptionMapper);
  }
}
