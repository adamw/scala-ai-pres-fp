package pres

import scala.caps.assumeSafe

object S301_Assume_safe:
  @assumeSafe
  def printToConsole(s: String): Unit = println(s)
