package wwaldron.eggs.infrastructure

import wwaldron.eggs.api.ApiModule
import wwaldron.eggs.domain.DomainModule

class Injector extends DomainModule with ApiModule with InfrastructureModule
