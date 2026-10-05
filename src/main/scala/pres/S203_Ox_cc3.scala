package pres

import language.experimental.captureChecking
import scala.util.boundary, boundary.break
import scala.caps.{any, Control, SharedCapability}

object S203_Ox_cc3:
  def firstIndex[T](xs: List[T], elem: T): Int =
    boundary:
      for (x, i) <- xs.zipWithIndex do
        if x == elem then break(i)
      -1

  //

  trait Ox extends SharedCapability
  trait Fork

  def supervised[T](t: Ox ?=> T): T = ???
  def fork(t: ->{any.except[Control]} Unit)(using ox: Ox): Fork^{ox} = ???

  // ok
  supervised:
    val _ = fork:
      val _ = boundary:
        break(1)

  // error
  // supervised:
  //   val _ = boundary:
  //     val _ = fork:
  //       break(1)
  //   ()    
