package wwaldron.eggs.domain

import scala.concurrent.Future

trait CookRepository {
  def findOne(): Future[Cook]
}
