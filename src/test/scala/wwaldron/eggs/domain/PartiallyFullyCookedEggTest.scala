package wwaldron.eggs.domain

import org.scalatest.FreeSpec
import org.scalatest.concurrent.Eventually
import wwaldron.eggs.domain.Egg.FullyCookedEgg

class PartiallyFullyCookedEggTest extends FreeSpec with DomainHelpers with Eventually {

  "isDone" - {
    "should return false if the egg has not cooked long enough" in {
      val egg = createPartiallyCookedEgg()

      assert(egg.isDone === false)
    }
    "should return true if the egg has cooked long enough" in {
      val egg = createPartiallyCookedEgg()

      eventually {
        assert(egg.isDone === true)
      }
    }
  }

  "finishCooking" - {
    "should return a PartiallyCookedEgg if the egg is not cooked" in {
      val egg = createPartiallyCookedEgg()

      assert(egg.finishCooking() === egg)
    }
    "should return a FullyCookedEgg if the egg is cooked" in {
      val egg = createPartiallyCookedEgg()

      eventually {
        val cookedEgg = egg.finishCooking()
        assert(cookedEgg.isInstanceOf[FullyCookedEgg])
        assert(cookedEgg.style === egg.style)
      }
    }
  }

}
