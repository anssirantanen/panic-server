package api

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import Directives._

import scala.concurrent.Await

trait MonitorWebsocketApi {
  val actorSystem : ActorSystem
  implicit val timeout : Timeout
  lazy val monitorWebsocketActor : ActorRef =Await.result(actorSystem.actorSelection("*/monitor-websocket-actor").resolveOne(timeout.duration),timeout.duration )


  def greeter: Flow[Message, Message, Any] =
    Flow[Message].mapConcat {
      case tm: TextMessage =>
        TextMessage(Source.single("Hello ") ++ tm.textStream ++ Source.single("!")) :: Nil

    }
  def websocketRoute: Route =
    path("greeter") {
      handleWebSocketMessages(greeter)
    }
}
