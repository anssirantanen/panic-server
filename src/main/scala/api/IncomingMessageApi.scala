package api

import IncomingMessage.IncomingMessageHandler
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.Await


trait IncomingMessageApi  {
  val actorSystem: ActorSystem
  implicit val timeout : Timeout
  lazy val incomingMessageHandler: ActorRef = actorSystem.actorOf(IncomingMessageHandler.props(),"IncomingMessageHandler")

def infoMessageApiRoutes : Route  =
  path("base"){
    post{
     entity(as[String]){ str =>
       val futureResponse = incomingMessageHandler ? IncomingMessageHandler.TextMessage(str)
       val result  = Await.result(futureResponse,timeout.duration).asInstanceOf[String]
       complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`,result))
     }
    }
  }
}
