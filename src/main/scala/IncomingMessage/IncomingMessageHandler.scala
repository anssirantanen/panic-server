package IncomingMessage

import IncomingMessage.IncomingMessageHandler.TextMessage
import akka.actor.{Actor, ActorLogging, Props}


object IncomingMessageHandler{
  case class TextMessage(textMessage: String)
  def props() : Props = Props(new IncomingMessageHandler)
}

class IncomingMessageHandler extends Actor with ActorLogging{

  log.error("Spinning actor")
  override def receive: Receive = {

    case message : TextMessage =>
      log.error("rebounding message")
      sender ! message.textMessage
  }
}
