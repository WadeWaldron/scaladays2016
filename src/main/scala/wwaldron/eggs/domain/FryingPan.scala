package wwaldron.eggs.domain

import wwaldron.eggs.domain.Egg.{CookedEgg, PartiallyCookedEgg, RawEgg}

trait FryingPan

case class EmptyFryingPan() extends FryingPan {
  def add(egg: RawEgg.type, style: EggStyle): FullFryingPan = FullFryingPan(egg.startCooking(style))
}

case class FullFryingPan(egg: PartiallyCookedEgg) extends FryingPan {
  def checkDoneness(): Boolean = egg.isDone
  def remove(): (EmptyFryingPan, CookedEgg) = (EmptyFryingPan(), egg.finishCooking())
}
