package wwaldron.eggs.api

import wwaldron.eggs.domain.Egg.CookedEgg
import wwaldron.eggs.domain.{CookRepository, EggStyle}

import scala.concurrent.{ExecutionContext, Future}

class FoodPrepApi(cookRepository: CookRepository)(implicit ec: ExecutionContext) {
  def prepareEgg(style: EggStyle): Future[CookedEgg] = {
    for {
      cook <- cookRepository.findOne()
      egg <- cook.prepareEgg(style)
    } yield egg
  }
}
