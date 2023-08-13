package my_part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorsIntro extends App {
  // 1 - instantiate actor-system
  val actorSystem = ActorSystem("myActorSystem")
  println(s"${actorSystem.name} was instantiated")

  // 2 - create actors
  class MyActor extends Actor {
    var msgCounter = 0

    // type Receive = PartialFunction[Any, Unit] // somewhere in Akka library code...
    override def receive: Receive = {
      case msg: String => {
        println(s"[word counter] message received: $msg")
        msgCounter += 1
        println(s"[word counter] counter incremented {counter = $msgCounter}")
      }
      case msg: Any => {
        println(s"[word counter] I can't understand message {$msg}")
      }
    }
  }

  object MyActor {
    def props = Props(new MyActor())
  }

  // 3 - instantiate actors within the ActorSystem and get an ActorRef instance
  //  val actor = actorSystem.actorOf(Props[MyActor], "myActor")
  val actor = actorSystem.actorOf(MyActor.props, "myActor")

  // 4 - send message to actor
  actor ! "first Akka message"

  // last - stop the ActorSystem
  actorSystem.terminate()
}
