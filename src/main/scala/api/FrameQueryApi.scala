package api

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import models.JsonTypeFormats
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._


object  FrameQueryApi  extends JsonTypeFormats{
 val actorSystem : ActorSystem =
 implicit val timeout : Timeout
 lazy val frameQueryHandler  : ActorRef = actorSystem.actorSelection("/user/IncomingFrameGuard/IncomingFrameHandler/monitor-websockets-actor").resolveOne(timeout.duration).value.get.get

  val listFrames : Route =
    path("frames"){
      get{
        complete("")
      }
    }
}
