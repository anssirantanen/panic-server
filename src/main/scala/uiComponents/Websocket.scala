package uiComponents

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSelection, Props}
import models.Frame
import uiComponents.Websocket.{ConnectToListener, OutgoingMessage, ToWebsocket}
import uiComponents.WebsocketListeners.ConnectToListeners
object  Websocket{
  trait WebsocketMessage
  case class ConnectToListener(outStream : ActorRef)
  case class ToWebsocket(text:String) extends WebsocketMessage
  case class OutgoingMessage(message: String)
  def props(websocketListeners: ActorSelection) = Props(new Websocket(websocketListeners))
}
class Websocket(websocketListeners : ActorSelection) extends Actor with ActorLogging{
  val socketContainer= websocketListeners

  override def receive : Receive= {
    case ConnectToListener(outStreamActor) =>
      context.become(connected(outStreamActor))
      websocketListeners ! ConnectToListeners
  }
  def connected(socket: ActorRef) : Receive ={
    case ToWebsocket(text:String) =>
      socket ! OutgoingMessage(text)
    case endpointMessage : Frame =>
      socket ! endpointMessage
  }
}


