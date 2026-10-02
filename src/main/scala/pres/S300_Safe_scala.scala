package pres

import language.experimental.safe

object S300_Safe_scala:
  def fib(n: Int): Int = if n <= 1 then 1 else fib(n - 1) + fib(n - 2)

  // def fib2(n: Int): Int =
  //   println(s"Computing for $n...")
  //   if n <= 1 then "1".asInstanceOf[Int] else fib(n - 1) + fib(n - 2)

  // capture checking + mutation checking + no type casts + only safe APIs
