package my_part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ActorCapabilities extends App {

  //  var foo: PartialFunction[Any, Unit] = {
  //    case param => println(s"received [$param] as parameter")
  //  }
  //
  //  foo.apply(42)
  //  foo.apply("my string")
  //  foo.apply(true)

  val actorSystem = ActorSystem("actorSystem")

  case class SimpleMessage(message: String)

  case class ActorMessage(message: String, actorRef: ActorRef)

  case class ForwardedMessage(message: String, ref: ActorRef)

  class SimpleActor extends Actor {
    private val maxReplies = 10;
    private var numReplies = 10;

    override def receive: Receive = {
      case msg: String => println(s"[SimpleActor] received String{$msg} from sender ${context.sender()}")
      case SimpleMessage(msg) => println(s"[SimpleActor] received SimpleMessage{$msg}")
      case ActorMessage(msg, actorRef) => {
        println(s"sender: ${context.sender()}")
        if (numReplies >= maxReplies) {
          println("max replies reached, stop replying...")
        }
        println(s"[SimpleActor] received ActorMessage: $msg")
        val reply = ActorMessage(s"reply to '$msg'", self)
        println(s"[SimpleActor] reply with message: ${reply.message}")
        actorRef ! reply
        numReplies += 1
      }
      case ForwardedMessage(message, ref) => ref forward message
    }
  }

  val simpleActor = actorSystem.actorOf(Props[SimpleActor])

  simpleActor ! "string message"
  simpleActor ! SimpleMessage("simple message")

  val alice = actorSystem.actorOf(Props[SimpleActor], "alice")
  val bob = actorSystem.actorOf(Props[SimpleActor], "bob")

//  alice ! ActorMessage("Hi, I'm Bob!", bob)

  alice ! ForwardedMessage("fwd message", bob)

  actorSystem.terminate()
}
