package pres

import java.time.{Clock, ZoneOffset}

object S208_Hidden_dependencies:

  object WithoutSkill:
    object DateMatcher:
      /** Worth bumping every few years. */
      private val ReferenceYear = 2025

      def yearSpace(year: Int): Int = math.max(math.abs(year - ReferenceYear), 20)

  object WithSkill:
    class DateMatcher(referenceYear: Int):
      def yearSpace(year: Int): Int = math.max(math.abs(year - referenceYear), 20)

    // wired once, at the edge, from the Clock the rest of the application already uses
    def build(clock: Clock) = DateMatcher(clock.instant().atZone(ZoneOffset.UTC).getYear)
