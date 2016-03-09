package wwaldron.eggs.api

import wwaldron.eggs.domain.DomainModule

import scala.concurrent.ExecutionContext

trait ApiModule { this: DomainModule =>
  implicit def executionContext: ExecutionContext
  val foodPrepApi: FoodPrepApi = new FoodPrepApi(cookRepository)
}

