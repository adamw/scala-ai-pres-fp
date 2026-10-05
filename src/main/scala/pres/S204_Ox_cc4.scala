package pres

import language.experimental.captureChecking
import language.experimental.separationChecking
import scala.util.boundary, boundary.break
import scala.caps.{any, Control, SharedCapability}

object S204_Ox_cc4:
  trait Ox extends SharedCapability
  trait Fork

  def supervised[T](t: Ox ?->{any.except[Control]} T): T = ???
  def fork(consume t: ->{any.except[Control]} Unit)(using ox: Ox): Fork^{ox} = ???

  // // ok
  // supervised:
  //   val _ = fork:
  //     var x = 10
  //     x += 1
  //     println(x)

  // // error
  // supervised:
  //   var x = 10
  //   val _ = fork:
  //     x += 1
  //     println(x)
  //   x += 2  
