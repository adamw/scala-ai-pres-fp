package pres

object S203_Boolean_blindness:

  def withoutSkill() =
    /** Boolean blindness: `true` at the call site says nothing. Two flags also admit a state that
      * cannot happen — a word is never both reversed and substituted.
      */
    case class Dictionary(rank: Int, reversed: Boolean, substituted: Boolean)

  def withSkill() =
    enum Spelling:
      case Plain, Reversed, Substituted

    case class Word(rank: Int, spelling: Spelling)
