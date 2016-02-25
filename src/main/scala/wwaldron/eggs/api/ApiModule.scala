package wwaldron.eggs.api

import wwaldron.eggs.domain.DomainModule

import scala.concurrent.ExecutionContext

trait ApiModule { this: DomainModule =>
  implicit def executionContext: ExecutionContext
  println("1: "+cookRepository)
  def foodPrepApi: FoodPrepApi = new FoodPrepApi(cookRepository)
}

