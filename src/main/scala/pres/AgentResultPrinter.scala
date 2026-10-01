package pres

import sttp.ai.core.agent.*

/** Pretty-prints everything an [[AgentResult]] carries: the answer, the tool calls with their
  * inputs/outputs, the full conversation and the token usage of each LLM call.
  */
object AgentResultPrinter:
  private val Width = 100

  def apply(result: AgentResult[Either[AgentFailure, String]]): Unit =
    println("╔" + "═" * (Width - 2) + "╗")
    println("║ AGENT RESULT" + " " * (Width - 15) + "║")
    println("╚" + "═" * (Width - 2) + "╝")

    result.finalAnswer match
      case Right(answer) => field("Answer", answer)
      case Left(failure) => field("Failed", failure.toString)
    field("Finish reason", result.finishReason.toString)
    field("Iterations", result.iterations.toString)

    section(s"Tool calls (${result.toolCalls.size})")
    for (call, i) <- result.toolCalls.zipWithIndex do
      println(f"${i + 1}%2d. ${call.toolName} [iteration ${call.iteration}, id ${call.id}]")
      field("in", call.input, indent = 6)
      field("out", call.output, indent = 6)

    section(s"Conversation (${result.history.entries.size})")
    for (entry, i) <- result.history.entries.zipWithIndex do
      entry match
        case ConversationEntry.UserPrompt(content) =>
          println(f"${i + 1}%2d. user")
          field("", content, indent = 6)
        case ConversationEntry.AssistantResponse(content, toolCalls) =>
          println(f"${i + 1}%2d. assistant")
          if content.nonEmpty then field("", content, indent = 6)
          for c <- toolCalls do field("calls", s"${c.toolName}(${c.input})", indent = 6)
        case ConversationEntry.ToolResult(_, toolName, result) =>
          println(f"${i + 1}%2d. tool result: $toolName")
          field("", result, indent = 6)
        case ConversationEntry.IterationMarker(current, max) =>
          println(f"${i + 1}%2d. -- iteration $current of $max --")

    section(s"Tokens (${result.llmCalls.size} LLM calls)")
    for (call, i) <- result.llmCalls.zipWithIndex do
      println(f"${i + 1}%2d. ${call.model.getOrElse("<unknown model>")}: ${usage(call.usage)}")
    field("Total", usage(result.usage))
    println()

  private def section(title: String): Unit =
    println()
    println(s"── $title " + "─" * math.max(0, Width - title.length - 5))

  /** Prints `label: value`, keeping multi-line values aligned under the first line. */
  private def field(label: String, value: String, indent: Int = 0): Unit =
    val prefix = " " * indent + (if label.isEmpty then "" else s"$label: ")
    val continuation = " " * prefix.length
    println(prefix + value.linesIterator.mkString("\n" + continuation))

  private def usage(u: TokenUsage): String =
    val extra = List(
      Option.when(u.cachedInputTokens.value > 0)(s"cached ${u.cachedInputTokens.value}"),
      Option.when(u.reasoningTokens.value > 0)(s"reasoning ${u.reasoningTokens.value}"),
      Option.when(u.cacheWriteInputTokens.value > 0)(
        s"cache write ${u.cacheWriteInputTokens.value}"
      )
    ).flatten
    s"in ${u.inputTokens.value}, out ${u.outputTokens.value}, total ${u.totalTokens.value}" +
      (if extra.isEmpty then "" else extra.mkString(" (", ", ", ")"))
