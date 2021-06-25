package nl.ritogames.agentcompiler;

import nl.ritogames.shared.exception.CompilationException;

/**
 * The interface Stage.
 *
 * @param <I> the type parameter
 * @param <O> the type parameter
 */
@FunctionalInterface
public interface Stage<I, O> {

  /**
   * Execute o.
   *
   * @param input the input
   * @return the o
   * @throws CompilationException the compilation exception
   */
  O execute(I input);

  /**
   * Pipe stage.
   *
   * @param <R>    the type parameter
   * @param source the source
   * @return the stage
   */
  default <R> Stage<I, R> pipe(Stage<O, R> source) {
    return value -> source.execute(execute(value));
  }
}
