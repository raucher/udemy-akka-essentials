package myconcurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
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
}
