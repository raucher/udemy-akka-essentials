package myconcurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object FuturesPromises extends App {

  def calculateMeaningOfLife(): Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife()
  }

  aFuture.onComplete({
    case Success(meaningOfLife) => println(s"Answer is: $meaningOfLife")
    case Failure(exception) => println(s"Error: ${exception.getMessage}")
  })

  Thread.sleep(3000)
}
