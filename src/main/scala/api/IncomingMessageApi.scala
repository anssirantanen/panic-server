package api

import IncomingMessage.IncomingMessageHandler
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.Segment
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import models.EndpointMessage
import scala.concurrent.Await
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

trait IncomingMessageApi   {

  val actorSystem: ActorSystem
  implicit val timeout : Timeout
  lazy val incomingMessageHandler: ActorRef = actorSystem.actorOf(IncomingMessageHandler.props(),"IncomingMessageHandler")

def infoMessageApiRoutes : Route  =

  path("base"){
    post{
     entity(as[EndpointMessage]){ message=>
       val futureResponse = incomingMessageHandler ? IncomingMessageHandler.TextMessage(message.name)
       val result  = Await.result(futureResponse,timeout.duration).asInstanceOf[String]
       //complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`,EndpointMessage(result)))
        complete(EndpointMessage(result))
     }
    }
  }
}
