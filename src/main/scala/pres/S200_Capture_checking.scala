package pres

import language.experimental.captureChecking
import java.io.{FileInputStream, InputStream}

object S200_Capture_checking:
  def withFile[T](name: String)(op: InputStream => T): T =
    val f = new FileInputStream(name)
    try op(f)
    finally f.close()

  def test1 = withFile("data.txt"): in =>
    in.read()

  def test2 = withFile("data.txt"): in =>
    in
