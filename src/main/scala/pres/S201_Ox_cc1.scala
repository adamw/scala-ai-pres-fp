package pres

import language.experimental.captureChecking
import scala.caps.SharedCapability

object S201_Ox_cc1:
  trait Ox extends SharedCapability
  trait Fork

  def supervised[T](t: Ox ?=> T): T = ???
  def fork(t: => Unit)(using Ox): Fork = ???

  // ok
  val _ = supervised:
    fork(println("1"))

  // should error
  val _ = supervised:
    println("x")
    // () => fork(println("2"))
