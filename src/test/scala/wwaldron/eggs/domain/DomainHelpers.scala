package wwaldron.eggs.domain

import wwaldron.eggs.domain.Egg.RawEgg

trait DomainHelpers {
  def createRawEgg() = RawEgg

  def createPartiallyCookedEgg(style: EggStyle = createEggStyle()) = createRawEgg().startCooking(style)

  def createCookedEgg(style: EggStyle = createEggStyle()) = {
    val egg = createRawEgg().startCooking(style)
    Thread.sleep(style.cookTime.toMillis * 2)
    egg.finishCooking()
  }

  def createEggStyle(): EggStyle = EggStyle.HardBoiled
}
