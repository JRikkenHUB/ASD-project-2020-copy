package nl.ritogames.trui.input;

import java.io.Reader;
import java.util.Scanner;

public class ScannerImpl implements IScanner {

  Scanner scanner;

  public ScannerImpl(Reader reader) {
    this.scanner = new Scanner(reader);
  }

  @Override
  public String nextLine() {
    return this.scanner.nextLine();
  }
}
