package my_part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ChangingActorBehaviour extends App {
  object FussyKid {
    val HAPPY = "happy"
    val SAD = "sad"

    case object Accept

    case object Reject
  }

  class FussyKid extends Actor {

    import FussyKid._
    import Mom._

    private var state = HAPPY

    override def receive: Receive = {
      case Food(VEGETABLES) =>
        println(s"[KID] receiving $VEGETABLES")
        println("[KID] becoming sad :(")
        state = SAD
      case Food(CHOCOLATE) =>
        println(s"[KID] receiving $CHOCOLATE")
        println("[KID] becoming happy :)")
        state = HAPPY

      case Mom.Ask(message) => state match {
        case HAPPY =>
          println(s"[KID] accepting '$message'")
          sender ! FussyKid.Accept
        case SAD =>
          println(s"[KID] rejecting '$message'")
          sender ! FussyKid.Reject
      }
    }
  }

  class ImmutableFussyKid extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def sadReceive: Receive = {
      case Food(CHOCOLATE) => context.become(happyReceive)
      case Food(VEGETABLES) =>
        println(s"[KID] receiving $VEGETABLES")
        println("[KID] becoming sad :(")
      case Mom.Ask(message) =>
        println(s"[KID] rejecting '$message'")
        sender ! Reject
    }

    def happyReceive: Receive = {
      case Food(VEGETABLES) => context.become(sadReceive)
      case Food(CHOCOLATE) =>
        println(s"[KID] receiving $CHOCOLATE")
        println("[KID] becoming happy :)")
      case Mom.Ask(message) =>
        println(s"[KID] accepting '$message'")
        sender ! Accept
    }
  }

  object Mom {

    val VEGETABLES = "veggies"
    val CHOCOLATE = "chocolate"

    case class MomStart(kidRef: ActorRef)

    case class Food(food: String)

    case class Ask(message: String)
  }

  class Mom extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = {
      case MomStart(kid) =>
        println(s"[MOM] Feeding kid with $VEGETABLES")
        kid ! Food(VEGETABLES)
        println("[MOM] asking kid to play")
        kid ! Ask("[MOM] lets play kiddo!")

        println(s"[MOM] Feeding kid with $CHOCOLATE")
        kid ! Food(CHOCOLATE)
        println("[MOM] asking kid to play")
        kid ! Ask("[MOM] lets play kiddo!")
      case Reject => println("[MOM] my suggestion to play was rejected")
      case Accept => println("[MOM] my suggestion to play was accepted")
    }
  }

  val actorSystem = ActorSystem("actorSystem")
  val mom = actorSystem.actorOf(Props[Mom], "mom")
  val kid = actorSystem.actorOf(Props[ImmutableFussyKid], "kid")

  mom ! Mom.MomStart(kid)

  actorSystem.terminate()
}
