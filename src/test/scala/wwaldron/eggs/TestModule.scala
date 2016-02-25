package wwaldron.eggs

import wwaldron.eggs.api.ApiModule
import wwaldron.eggs.domain.{CookRepository, EggRepository, DomainModule}
import wwaldron.eggs.infrastructure.{InMemoryEggRepository, InMemoryCookRepository}

import scala.concurrent.ExecutionContext

trait TestModule extends DomainModule with ApiModule {
  override val eggRepository: EggRepository = new InMemoryEggRepository

  override val cookRepository: CookRepository = new InMemoryCookRepository(eggRepository)

  override implicit def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
}
