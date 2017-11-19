package uiComponents

import IncomingFrame.IncomingFrameHandler.TextMessage
import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import models.Frame
import uiComponents.Websocket.{ConnectToListener, ToWebsocket}
import uiComponents.WebsocketListeners.ConnectToListeners
object  WebsocketListeners {
  case class ConnectToListeners()
  def props() : Props = Props(new WebsocketListeners)
}

class WebsocketListeners  extends Actor with ActorLogging{

  var websockets : Set[ActorRef]= Set.empty

  override def receive: Receive = {
    case message :TextMessage =>
      websockets.foreach(socket => socket ! ToWebsocket(message.textMessage))
    case endpointMessage : Frame =>
      websockets.foreach( socket => socket ! endpointMessage)
    case ConnectToListeners =>
      websockets += sender()
      context.watch(sender())
    case Terminated(socket) =>
      websockets -= socket
  }

}
