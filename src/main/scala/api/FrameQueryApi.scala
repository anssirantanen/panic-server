package api

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import models.JsonTypeFormats
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._


trait FrameQueryApi  extends JsonTypeFormats{
 val actorSystem : ActorSystem
  implicit val timeout : Timeout

  val listFrames : Route =
    path("frames"){
      get{
        complete("")
      }
    }
}
