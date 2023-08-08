package playground

import akka.actor.ActorSystem

import scala.util.Try

object MyPlayground extends App {
  val actorSystem = ActorSystem("myActorSystem")

  println(actorSystem.name)

  val aTry = Try {
    throw new RuntimeException()
  }

  println(aTry)
}
