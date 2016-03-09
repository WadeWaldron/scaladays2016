package wwaldron.eggs.infrastructure

import wwaldron.eggs.api.ApiModule
import wwaldron.eggs.domain.{CookRepository, EggRepository, DomainModule}

import scala.concurrent.ExecutionContext

trait InfrastructureModule { this: ApiModule with DomainModule =>
  override val eggRepository: EggRepository = new InMemoryEggRepository()

  override val cookRepository: CookRepository = new InMemoryCookRepository(eggRepository)

  override implicit def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
}
