package pres

object S204_Domain_types:

  object WithoutSkill:
    object PasswordScore:
      val Min = 0
      val Max = 4

    /** A password's strength is a number from 0 to 4 — but `Int` also admits 7. A smart constructor
      * is the only way in, so out-of-range is rejected once, in one place.
      */
    case class PasswordAnalysis(score: Int)

  object WithSkill:
    enum Score(val value: Int):
      case TooGuessable extends Score(0)
      case VeryGuessable extends Score(1)
      case Guessable extends Score(2)
      case Strong extends Score(3)
      case VeryStrong extends Score(4)

    object Score:
      def fromInt(value: Int): Option[Score] = values.find(_.value == value)
