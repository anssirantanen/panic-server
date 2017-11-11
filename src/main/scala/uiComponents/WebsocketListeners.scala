package uiComponents

import IncomingMessage.IncomingMessageHandler.TextMessage
import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import uiComponents.Websocket.{ConnectToListener, ToWebsocket}
import uiComponents.WebsocketListeners.ConnectToListeners
object  WebsocketListeners {
  case class ConnectToListeners()
  def props() : Props = Props(new WebsocketListeners)
}

class WebsocketListeners  extends Actor with ActorLogging{

  var websockets : Set[ActorRef]= Set.empty

  override def receive = {
    case message :TextMessage =>
      log.info(s"received ${message.textMessage}")
      websockets.foreach(socket => socket ! ToWebsocket(message.textMessage))
    case ConnectToListeners =>
      websockets += sender()
      context.watch(sender())

    case Terminated(socket) =>
      websockets -= socket
  }

}
