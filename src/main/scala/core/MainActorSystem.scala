package core

import akka.actor.ActorSystem

object MainActorSystem {
  private val system = ActorSystem("main")
  def get: ActorSystem = {
    system
  }
}
