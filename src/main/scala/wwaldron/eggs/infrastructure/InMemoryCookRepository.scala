package wwaldron.eggs.infrastructure

import wwaldron.eggs.domain.{EggRepository, Cook, CookRepository}

import scala.concurrent.{ExecutionContext, Future}

class InMemoryCookRepository(eggRepository: EggRepository)(implicit ec: ExecutionContext) extends CookRepository {
  override def findOne(): Future[Cook] = {
    Future.successful(new Cook(eggRepository))
  }
}
