package wwaldron.eggs

import wwaldron.eggs.api.ApiModule
import wwaldron.eggs.domain.DomainModule
import wwaldron.eggs.infrastructure.InfrastructureModule

trait TestModule extends InfrastructureModule with ApiModule with DomainModule
