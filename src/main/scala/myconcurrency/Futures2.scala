package myconcurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success}

object Futures2 extends App {

  val future1 = Future {
    42
  }

  val future2 = Future {
    3
  }

  future1.flatMap(n1 => future2.map(n2 => n1 * n2))

  val aMb = for {
    a <- future1
    b <- future2
  } yield a * b

  aMb.onComplete({
    case Success(value) => println(value)
    case Failure(exception) => exception.printStackTrace()
  })

  val ft = Future(25).filter(_ == 12).recover {
    case e: NoSuchElementException => {
      println("Element not found")
      42
    }
    case e: Exception => println("Generic error occurred")
  }

  ft.onComplete {
    case Success(v) => println(s"ft has success with value [$v]")
    case Failure(e) => e.printStackTrace()
  }

  Await.result(ft, 2.seconds)

  //  Element not found
  //  126
  //  ft has success with value [42]
  //
  //  Process finished with exit code 0
  val promise = Promise()

  def retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T] =
    action().filter(condition).recoverWith { case _ => retryUntil(action, condition) }

}
