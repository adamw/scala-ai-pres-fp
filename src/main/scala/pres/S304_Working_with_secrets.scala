package pres

import language.experimental.safe
import S303_API_for_secrets.*

class S304_Working_with_secrets(using IO):

  val x1 = Classified.read("x")
  val x2 = x1.map(_.toUpperCase())

  FileSystem.request: fs =>
    fs.write("yy", fs.read("y") * 2)

  // x1.map(v => println(v))

  // FileSystem.request: fs =>
  //   x1.map: v =>
  //     fs.write("yy", v)

  // x1.map: v =>
  //   FileSystem.request: fs =>
  //     fs.write("yy", v)
