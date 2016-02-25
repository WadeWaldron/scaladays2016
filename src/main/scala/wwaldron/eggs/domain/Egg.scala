package wwaldron.eggs.domain

case class Timestamp(value: Long = System.currentTimeMillis())

sealed trait Egg

object Egg {
  case object RawEgg extends Egg {
    def startCooking(style: EggStyle): PartiallyCookedEgg = PartiallyCookedEgg(style)
  }

  sealed trait CookedEgg extends Egg {
    def style: EggStyle
  }

  case class PartiallyCookedEgg private[Egg](style: EggStyle) extends CookedEgg {
    private val startTime: Timestamp = Timestamp()
    def isDone: Boolean = (System.currentTimeMillis() - startTime.value) > style.cookTime.toMillis

    def finishCooking(): CookedEgg = {
      if(isDone) {
        FullyCookedEgg(style)
      } else {
        this
      }
    }
  }

  case class FullyCookedEgg private[Egg](style: EggStyle) extends CookedEgg
}
