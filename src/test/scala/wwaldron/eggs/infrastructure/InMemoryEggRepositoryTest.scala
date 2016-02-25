package wwaldron.eggs.infrastructure

import org.scalatest.FreeSpec
import org.scalatest.concurrent.ScalaFutures
import wwaldron.eggs.domain.DomainHelpers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InMemoryEggRepositoryTest extends FreeSpec with ScalaFutures with DomainHelpers {

  class TestContext {
    val repo = new InMemoryEggRepository
  }

  "add" - {
    "should add an egg to the repo" in new TestContext {
      val egg = createRawEgg()

      val futureResult = for {
        _ <- repo.add(egg)
        egg <- repo.findAndRemove()
      } yield egg

      whenReady(futureResult) { result =>
        assert(result === Some(egg))
      }
    }
  }

  "findAndRemove" - {
    "should return None if there are no eggs in the repo" in new TestContext {
      whenReady(repo.findAndRemove()) { result =>
        assert(result === None)
      }
    }
    "should continue to return eggs as long as they are in the repo." in new TestContext {
      val eggs = (1 to 10).map(_ => createRawEgg())

      val addEggs = Future.sequence(eggs.map(egg => repo.add(egg)))
      val removeEggs = addEggs.flatMap { result =>
        Future.sequence(result.map(_ => repo.findAndRemove()))
      }

      whenReady(removeEggs) { results =>
        assert(results.flatten === eggs)

        whenReady(repo.findAndRemove()) { finalResult =>
          assert(finalResult === None)
        }
      }
    }
  }

}
