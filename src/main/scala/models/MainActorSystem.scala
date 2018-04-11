package models

import akka.actor.ActorSystem

object MainActorSystem {
  sealed val system = ActorSystem("main")
  def get() = {
    system
  }
}
