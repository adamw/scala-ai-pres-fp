package pres

import language.experimental.safe

object S305_TACIT:

  // TACIT: Tracked Agent Capabilities In Types
  // https://github.com/lampepfl/tacit

  requestFileSystem("/project") {
    // OK: read classified content
    val doc = readClassified("secrets/contract-v2.txt")

    // OK: pure transformation
    val upper = doc.map(_.trim)

    // OK: send to trusted local LLM, result stays Classified
    val summary = chat(doc.map(s => s"Summarize the following document:\n$s"))
    // summary: Classified[String], content is still protected

    // OK: write back to a classified file
    writeClassified("secrets/summary.txt", summary)
  }

  requestNetwork(Set("api.example.com")) {
    requestFileSystem("/project") {
      val key = readClassified("secrets/api.key")

      // OK: the token reaches the allowlisted host as a header, but is never
      // observable to agent code (the value cannot be printed or inspected).
      val me = httpGet(
        "https://api.example.com/me",
        secretHeaders = Map("Authorization" -> key.map("Bearer " + _))
      )

      // OK: secret body in, Classified response out.
      val payload = readClassified("secrets/report.json")
      val reply = httpPostClassified("https://api.example.com/process", payload)
      // reply: Classified[String]
    }
  }

  // defs
  trait Classified[T]:
    def map[U](f: T => U): Classified[U]
  def requestFileSystem(p: String)(t: => Unit): Unit = ???
  def requestNetwork(p: Set[String])(t: => Unit): Unit = ???
  def readClassified(p: String): Classified[String] = ???
  def chat(v: Any): Unit = ???
  def writeClassified(p: String, v: Any) = ???
  def httpGet(u: String, secretHeaders: Map[String, Classified[String]]) = ???
  def httpPostClassified(u: String, payload: Any): Classified[String] = ???
