package wwaldron.eggs.domain

import org.scalatest.FreeSpec

class RawEggTest extends FreeSpec with DomainHelpers {
  "startCooking" - {
    "should return a partially cooked egg" in {
      val style = createEggStyle()
      val rawEgg = createRawEgg()

      val partiallyCookedEgg = rawEgg.startCooking(style)

      assert(partiallyCookedEgg.style === style)
    }
  }
}
