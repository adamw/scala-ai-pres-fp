package pres

import language.experimental.captureChecking
import scala.caps.assumeSafe
import scala.caps.SharedCapability

// Based on "Tracking Capabilities for Safer Agents" https://arxiv.org/pdf/2603.00991

@assumeSafe
object S303_API_for_secrets:

  trait Classified[+T]:
    def map[U](op: T -> U): Classified[U]

  object Classified:
    def read(path: String): Classified[String] = ???
    def write(path: String, c: Classified[String]): Unit = ???

  //

  trait IO extends SharedCapability

  trait FileSystem:
    def read(path: String): String
    def write(path: String, content: String): Unit

  object FileSystem:
    def request[T](f: FileSystem^ => T)(using IO): T = ???  