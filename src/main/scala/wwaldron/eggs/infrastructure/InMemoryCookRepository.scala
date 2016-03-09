package wwaldron.eggs.infrastructure

import wwaldron.eggs.domain.{CookId, EggRepository, Cook, CookRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class InMemoryCookRepository(eggRepository: EggRepository)(implicit ec: ExecutionContext) extends CookRepository {
  override def findOne(): Future[Cook] = {
    Future.successful(new Cook(CookId(Random.nextInt()))(eggRepository))
  }
}
