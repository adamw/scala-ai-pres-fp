package pres

object S205_Recoverable_failures:
  import S204_Domain_types.WithSkill.Score

  def withoutSkill() =
    case class PasswordConfig(minScore: Int) derives ConfigReader:
      /** A typo in a config file is a recoverable failure. `assert` throws instead — and disappears
        * entirely under `-Xdisable-assertions`.
        */
      assert(minScore >= 0 && minScore <= 4, s"min-score is $minScore, must be 0 to 4.")

  def withSkill() =
    case class PasswordPolicyConfig(minScore: Score)

    object PasswordPolicyConfig:
      /** The shape in the file, before it is checked. Private: the validated type has no other
        * constructor a config file can reach.
        */
      private case class Raw(minScore: Int) derives ConfigReader

      given ConfigReader[PasswordPolicyConfig] = ConfigReader[Raw].emap: raw =>
        Score
          .fromInt(raw.minScore)
          .map(PasswordPolicyConfig.apply)
          .toRight(InvalidPolicy(s"min-score must be 0 to 4, is ${raw.minScore}"))

    case class InvalidPolicy(message: String) extends FailureReason

  // --- PureConfig, mocked out ---

  /** `emap` narrows a reader to one that may reject what it read. */
  trait ConfigReader[T]:
    def emap[U](f: T => Either[FailureReason, U]): ConfigReader[U] = new ConfigReader[U] {}

  object ConfigReader:
    def apply[T](using reader: ConfigReader[T]): ConfigReader[T] = reader
    def derived[T]: ConfigReader[T] = new ConfigReader[T] {}

  trait FailureReason
