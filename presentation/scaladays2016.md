# Domain Driven Design and Onion Architecture in Scala
### - Wade Waldron (@wdwaldron) -
### - Senior Software Developer with BoldRadius -

---

## Case Study

![right fit](img/fried_egg.jpg)

### How to fry an egg with Domain Driven Design and Onion Architecture

---

^ Emphasizes capturing the truth of the world in the software model

^ Collaboration between technical and domain experts using a common language

## What is Domain Driven Design?

- Domain: A sphere of knowledge
- Domain Driven Design: A technique for developing software that puts the primary focus on the core domain

---

## Ubiquitous Language

- A common language that can be used by domain experts and technical experts
- Reflected in the software model

---

## Case Study: Language

![left fit](img/chef.jpg)

- Cook
- Egg, Sunny Side Up, Scrambled
- Stove, Frying Pan

---

## Bounded Contexts

- The setting or context where words from the ubiquitous language apply
- Outside of the bounded context, the words meaning may change, or it may not apply
- Bounded Contexts fit well with Microservice Architectures

---

## Case Study: Bounded Contexts

![left fit](img/professional_kitchen.jpg)

- **Food Preparation**
- Grocery Shopping
- Washing Dishes


---

## Domain Building Blocks

- Value Objects: A domain object that is defined by it's attributes
- Entity: A domain object that is defined by an identity
- Aggregate: A collection of objects bound by a root entity
- Service: A container for operations that don't conceptually fit other domain objects
- Repository: An abstraction layer for retrieving existing instances of domain objects
- Factory: An abstraction layer for creating new instances of domain objects

---

## Case Study: Domain Objects

![left fit](img/eggs.jpg)

- Cook, CookFactory, CookRepository
- Egg, EggStyle
- FryingPan

---

## What is Onion Architecture

![right fit](img/onion_architecture.png)

- Application is built around the domain
- Outer layers depend on and are coupled to the inner layers
- Inner layers are decoupled from the outer layers
- Inner layers define interfaces that may be implemented in the outer layers

---

## Onion Architecture Layers

![right fit](img/onion_architecture.png)

- Core: Basic building blocks. Collections, Akka, Scalaz etc.
- Domain: Key domain/business logic. Entities, Aggregates, Repositories, Services etc.
- API: Defines the "public" interface to the Domain.
- Infrastructure: External Dependencies, User Interfaces, Database etc.

---

# API

---

## Decoupling the Domain

![fit left](img/broken_chain.jpg)

- API Insulates the Domain from the Infrastructure
- Provides a single consistent code interface
- Domain can be restructured/rewritten without affecting the API
- API is a good place for high level functional tests

---

## Case Study: The First Crack

^ SLIDE 1/2

```scala
trait FoodPrepApi {
  def fry(egg: Egg): FriedEgg
}
```
---

## Case Study: The First Crack

^ SLIDE 2/2

```scala
trait FoodPrepApi {
  def fry(egg: Egg): FriedEgg
}
```

- Problems:
    - Who is frying the egg?
    - Doesn't capture the time to cook the egg.
    - Is the Egg Over Easy? Scrambled? Sunny Side Up?

---

## Case Study: Second Attempt

```scala
trait FoodPrepApi {
  def prepareEgg(style: EggStyle): Future[CookedEgg]
}
```

- Still don't know who is frying the Egg (Does that matter?).
- We now capture that it takes time using a Future.
- We allow for different types of fried eggs using EggStyle and CookedEgg.

---

# Domain

---

## Algebraic Data Types

- Useful for building rich domains
- Provide added type safety
- Capture truth of the domain at compile time

---

## Case Study: How would you like your eggs?

```scala
sealed trait EggStyle

object EggStyle {
  case object Scrambled extends EggStyle
  case object SunnySideUp extends EggStyle  
  case object Poached extends EggStyle
}

sealed trait Egg

case object RawEgg extends Egg
case class PartiallyCookedEgg(style: EggStyle) extends Egg
case class FullyCookedEgg(style: EggStyle) extends Egg
```

---

## Case Study: Is it done yet?

^ The purpose of this test is to perform the necessary steps with the minimal amount of mocking/stubbing.

^ This test will act like our canary in the coal mine.

^ Other Unit Tests will handle testing the various edge cases.  This is a simple happy path test.

```scala
class FoodPrepApiTest 
  extends FreeSpec 
  with ScalaFutures {

  class TestContext extends TestModule

  "prepareEgg" - {
    "should return a CookedEgg with the specified style" in new TestContext {
      whenReady(foodPrepApi.prepareEgg(EggStyle.SunnySideUp)) { egg =>
        assert(egg == CookedEgg(EggStyle.SunnySideUp))
      }
    }
  }
}
```

---

## Evolving Truth

^ Domain Driven Design is about evolving our understanding of the domain, and reflecting that evolution in the code.

^ The techniques of Domain Driven Design allow us to keep our model decoupled from other areas of the system so that we can safely evolve it.

^ Onion Architecture in turn assists in this decoupling process.
 
^ API layer ensures the Domain and Infrastructure remain separate and distinct instead of bleeding into each other.

^ Often when building the domain model we find rough edges. We find areas where the code feels awkward.  This is usually a sign
that we have the model wrong.  We need to look for these rough edges and try to draw out what is really happening.  We then
evolve the model appropriately.

^ Sometimes the domain itself changes.  A new law may cause us to have to do our work differently.  A new technology may
change the way people work in the business.  When the domain changes the model needs to change with it.

![](img/evolution.jpg)

- We never know the truth of the domain until we have spent time with it.

---

## Case Study: Broken Frying Pan

^ Frying pan takes an option.  This means checking all the time if the egg is present.

^ We are returning Try which means wrapping exceptions.

^ What cook tries to add another egg to an already full frying pan?

^ What cook tries to remove an egg from an empty frying pan?

^ This model is awkward to use, awkward to test, and basically wrong.

```scala
case class FryingPan(cookingEgg: Option[PartiallyCookedEgg] = None) {
  import FryingPan._

  def add(egg: RawEgg, desiredStyle: EggStyle): Try[FryingPan] = {
    cookingEgg match {
      case Some(_) => Failure(FryingPanFullException)
      case None => Success(this.copy(Some(egg.startCooking(desiredStyle))))
    }
  }

  def remove(): Try[(FryingPan, CookedEgg)] = {
    cookingEgg match {
      case Some(egg) => egg.finishCooking().map(cookedEgg => (this.copy(None),cookedEgg))
      case None => Failure(FryingPanEmptyException)
    }
  }
}
```

---

## Case Study: Fixed Frying Pan

^ The cook won't try to remove an egg from an empty frying pan or add one to a full frying pan. They literally can't.
The compiler won't allow it.

```scala
trait FryingPan

case class EmptyFryingPan() extends FryingPan {
  def add(egg: RawEgg, style: EggStyle): FullFryingPan = FullFryingPan(egg.startCooking(style))
}

case class FullFryingPan(egg: PartiallyCookedEgg) extends FryingPan {  
  def remove(): (EmptyFryingPan, CookedEgg) = (EmptyFryingPan(), egg.finishCooking())
}
```

- No more Options
- No more Try
- No more Exceptions

---

# Infrastructure

---

## Dependency Inversion Principle

> High-level modules should not depend on low-level modules. Both should depend on abstractions.

- Onion Architecture relies on the Dependency Inversion Principle
- Domain often defines traits that are then implemented in Infrastructure
- Dependency Injection can be used to implement inversion of control
