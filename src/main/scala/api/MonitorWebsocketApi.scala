package api

import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, PoisonPill}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import Directives._
import akka.NotUsed
import akka.event.Logging
import akka.event.jul.Logger
import akka.http.javadsl.model.ws.WebSocket
import core.MainActorSystem
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}
import models.{Frame, JsonTypeFormats}
import io.circe.syntax._
import io.circe.generic.auto._


/*
*  Websocket handling implemented by using:
* https://github.com/calvinlfer/akka-http-streaming-response-examples/blob/master/src/main/scala/com/experiments/calvin/ws/WebSocketRoutes.scala
* */


import scala.concurrent.Await

object MonitorWebsocketApi extends  JsonTypeFormats{

  val actorSystem : ActorSystem = MainActorSystem.get
  lazy val monitorWebsocketActor = actorSystem.actorSelection("/user/IncomingFrameGuard/IncomingFrameHandler/monitor-websockets-actor")

  def newWebsocketConnection() : Flow[Message, Message, NotUsed]={

    val connectedWsActor = actorSystem.actorOf(uiComponents.Websocket.props(monitorWebsocketActor))
    val incoming: Sink[Message,NotUsed]  =
      Flow[Message].filter(_ => false).to(Sink.actorRef(connectedWsActor,PoisonPill))
    val outgoingMessage: Source[Message,NotUsed] = Source
        .actorRef[Frame](10,OverflowStrategy.fail)
        .mapMaterializedValue{outgoingActor =>
          connectedWsActor ! uiComponents.Websocket.ConnectToListener(outgoingActor)
          NotUsed
        }.map{
      message : Frame =>
        val json = message.asJson
        TextMessage(json.toString())
    }

    Flow.fromSinkAndSource(incoming, outgoingMessage)
  }
  def websocketRoute: Route =
    path("sock") {
      handleWebSocketMessages(newWebsocketConnection())
    }
}
