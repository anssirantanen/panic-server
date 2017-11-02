package IncomingMessage

import IncomingMessage.IncomingMessageHandler.TextMessage
import akka.actor.{Actor, ActorLogging, Props}


object IncomingMessageHandler{
  case class TextMessage(textMessage: String)
  def props() : Props = Props(new IncomingMessageHandler)
}

class IncomingMessageHandler extends Actor with ActorLogging{

  log.debug("Spinning actor")
  override def receive: Receive = {

    case message : TextMessage =>
      log.debug("rebounding message")
      sender ! message.textMessage
  }
}
