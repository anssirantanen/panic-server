package api

import java.time.ZonedDateTime

import IncomingFrame.IncomingFrameHandler
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import models.{Frame, JsonTypeFormats}

import scala.concurrent.Await
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.auto._

trait IncomingFrameApi extends  JsonTypeFormats{

  val actorSystem: ActorSystem
  implicit val timeout : Timeout
  lazy val incomingFrameHandler : ActorRef =Await.result(actorSystem.actorSelection("/user/IncomingFrameGuard/IncomingFrameHandler").resolveOne(timeout.duration),timeout.duration )

  def infoFrameApiRoutes : Route  =
    path("base"){
      post{
        entity(as[Frame]){ message=>
          incomingFrameHandler ! message
          complete(message)
        }
      }
  }
}
