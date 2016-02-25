package wwaldron.eggs.domain

import wwaldron.eggs.domain.Egg.CookedEgg

import scala.concurrent.{ExecutionContext, Future}

object Cook {
  case object OutOfEggsException extends IllegalStateException("There are no more eggs")
}

class Cook(eggRepository: EggRepository)(implicit ec: ExecutionContext) {
  import Cook._
  private val fryingPan = new EmptyFryingPan()
//
  private def wait(pan: FullFryingPan) = {
    Stream
      .continually(pan.checkDoneness())
      .takeWhile(_ == false)
  }

  def prepareEgg(style: EggStyle): Future[CookedEgg] = {
    eggRepository.findAndRemove().map { eggOption =>
      val egg = eggOption.getOrElse(throw OutOfEggsException)

      val fullFryingPan = fryingPan.add(egg, style)
      wait(fullFryingPan)
      fullFryingPan.remove()._2
    }
  }
//  {
//    val x = eggRepository.findAndRemove().map { eggOption =>
//      eggOption.map { egg =>
//        fryingPan.add(egg, style).flatMap { fullPan =>
//          wait(fullPan)
//          fullPan.remove() match {
//            case
//          }
//        }.get
//      }
//    }
//    x
//  }
}
