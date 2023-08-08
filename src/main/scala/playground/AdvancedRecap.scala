package playground

import scala.concurrent.Future

object AdvancedRecap extends App {

  val partialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 65
    case 5 => 999
  }

  val pf = (x: Int) => x match {
    case 1 => 42
    case 2 => 65
    case 5 => 999
  }

  val modifiedList = List(1, 2, 3).map {
    case 1 => true
    case _ => false
  }

  partialFunction.orElse[Int, Int] {
    case 66 => 9000
  }

  type ReceiveFunction = PartialFunction[Int, Unit]

  def rf: ReceiveFunction = {
    case 2 => println(7)
    case _ => println("error...")
  }

  import scala.concurrent.ExecutionContext.Implicits.global

  Future {
    println("future's code...")
  }
}
