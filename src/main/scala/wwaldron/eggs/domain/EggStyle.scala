package wwaldron.eggs.domain

import scala.concurrent.duration._

sealed trait EggStyle {
  def cookTime = 10.millis
}

object EggStyle {
  case object Scrambled extends EggStyle
  case object SunnySideUp extends EggStyle
  case object OverEasy extends EggStyle
  case object OverMedium extends EggStyle
  case object OverHard extends EggStyle
  case object HardBoiled extends EggStyle
  case object SoftBoiled extends EggStyle
  case object Poached extends EggStyle
}
