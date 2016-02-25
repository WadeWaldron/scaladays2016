package wwaldron.eggs.firstcrack.api

import wwaldron.eggs.firstcrack.domain.{FriedEgg, Egg}

trait FoodPrepApi {
  def fry(egg: Egg): FriedEgg
}
