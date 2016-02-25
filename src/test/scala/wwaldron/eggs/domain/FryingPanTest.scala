package wwaldron.eggs.domain

import org.scalatest.FreeSpec
import org.scalatest.concurrent.Eventually

class FryingPanTest extends FreeSpec with DomainHelpers with Eventually {

  class TestContext {
    val fryingPan = EmptyFryingPan()
  }

  "add" - {
    "should add an egg to the pan if it is empty" in new TestContext {
      val egg = createRawEgg()
      val style = createEggStyle()

      val result = fryingPan.add(egg, style)

      assert(result === FullFryingPan(egg.startCooking(style)))
    }
  }

  "checkDoneness" - {
    "should return false if the egg is not ready" in new TestContext {
      val style = createEggStyle()
      val egg = createRawEgg()
      val expectedEgg = createCookedEgg(style)

      val fullFryingPan = fryingPan.add(egg, style)

      assert(fullFryingPan.checkDoneness() === false)
    }
    "should return true if the egg is ready" in new TestContext {
      val style = createEggStyle()
      val egg = createRawEgg()
      val expectedEgg = createCookedEgg(style)

      val fullFryingPan = fryingPan.add(egg, style)

      eventually {
        assert(fullFryingPan.checkDoneness() === true)
      }
    }
  }

  "remove" - {
    "should return a cooked egg if there is one in the pan" in new TestContext {
      val style = createEggStyle()
      val egg = createRawEgg()
      val expectedEgg = createCookedEgg(style)

      val fullFryingPan = fryingPan.add(egg, style)

      eventually {
        assert(fullFryingPan.remove() === (EmptyFryingPan(), expectedEgg))
      }
    }
  }
}
