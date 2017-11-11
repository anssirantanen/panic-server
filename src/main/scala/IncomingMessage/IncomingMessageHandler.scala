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

  log.info("Spinning actor")
  override def receive: Receive = {

    case message : TextMessage =>
      log.info("rebounding message")
      websocketListeners ! message
      sender ! message.textMessage
  }
}
