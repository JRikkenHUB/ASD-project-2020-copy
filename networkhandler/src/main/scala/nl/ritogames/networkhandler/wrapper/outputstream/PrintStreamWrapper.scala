package nl.ritogames.networkhandler.wrapper.outputstream

import java.io.{OutputStream, PrintStream}

class PrintStreamWrapper(val out: OutputStream) extends OutputStreamWrapper {
  val printStream: PrintStream = new PrintStream(out)

  override def println(s: String): Unit = {
    printStream.println(s)
  }
}
