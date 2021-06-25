package nl.ritogames.shared.exception;

import java.io.IOException;

public class ASDIOException extends RuntimeException {

  public ASDIOException(IOException e) {
    super(e);
  }
}
