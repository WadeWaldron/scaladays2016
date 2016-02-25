package wwaldron.eggs.domain

import wwaldron.eggs.domain.Egg.RawEgg

import scala.concurrent.Future

trait EggRepository {
  def findAndRemove(): Future[Option[RawEgg.type]]
  def add(egg: RawEgg.type): Future[Unit]
}
