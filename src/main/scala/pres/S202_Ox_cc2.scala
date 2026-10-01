package pres

import language.experimental.captureChecking
import scala.caps.SharedCapability

object S202_Ox_cc2:
  trait Ox extends SharedCapability
  trait Fork:
    def join(): Unit

  def supervised[T](t: Ox ?=> T): T = ???
  def fork(t: => Unit)(using ox: Ox): Fork^{ox} = ???

  val _ = supervised:
    // ok
    val f = fork(println("1"))
    f.join()

//   val f = supervised:
//     // should error
//     println("x")
//     fork(println("2"))

//   f.join()
