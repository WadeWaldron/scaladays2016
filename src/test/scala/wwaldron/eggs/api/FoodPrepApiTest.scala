package wwaldron.eggs.api

import org.scalatest.FreeSpec
import org.scalatest.concurrent.ScalaFutures
import wwaldron.eggs.TestModule
import wwaldron.eggs.domain.{DomainHelpers, EggStyle}

class FoodPrepApiTest
  extends FreeSpec
  with ScalaFutures
  with DomainHelpers {

  "prepareEgg" - {
    "should return a CookedEgg with the specified style" in new TestModule {
      eggRepository.add(createRawEgg())

      val expectedEgg = createCookedEgg(EggStyle.SunnySideUp)

      whenReady(foodPrepApi.prepareEgg(EggStyle.SunnySideUp)) { egg =>
        assert(egg == expectedEgg)
      }
    }
  }

}
