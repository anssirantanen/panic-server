package api

import akka.actor.{ActorRef, ActorSelection, ActorSystem}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import models.JsonTypeFormats
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import core.MainActorSystem
import frameQuerry.FrameQueryHandler


object  FrameQueryApi  extends JsonTypeFormats{
 val actorSystem : ActorSystem = MainActorSystem.get
 lazy val frameQueryHandler: ActorRef = actorSystem.actorOf(FrameQueryHandler.props)

  val listFrames : Route =
    path("frames"){
      get{
        complete("")
      }
    }
}
