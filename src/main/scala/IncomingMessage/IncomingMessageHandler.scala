package IncomingMessage

import IncomingMessage.IncomingMessageHandler.TextMessage
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import uiComponents.WebsocketListeners


object IncomingMessageHandler{
  case class TextMessage(textMessage: String)
  def props() : Props = Props(new IncomingMessageHandler)
}

class IncomingMessageHandler extends Actor with ActorLogging{
  val websocketListeners: ActorRef = context.actorOf(WebsocketListeners.props(), "monitor-websockets-actor")

  override def receive: Receive = {

    case message : TextMessage =>
      websocketListeners ! message
      sender ! message.textMessage
  }
}
