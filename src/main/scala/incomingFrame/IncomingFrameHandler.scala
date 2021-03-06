package incomingFrame

import incomingFrame.IncomingFrameHandler.TextMessage
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import models.Frame
import uiComponents.WebsocketListeners


object IncomingFrameHandler{
  case class TextMessage(textMessage: String)
  def props : Props = Props(new IncomingFrameHandler)
}

class IncomingFrameHandler extends Actor with ActorLogging{

  val websocketListeners: ActorRef = context.actorOf(WebsocketListeners.props(), "monitor-websockets-actor")
  override def receive: Receive = {
    case message : TextMessage =>
      websocketListeners ! message
    case endPointMessage : Frame =>
      log.info("frame received")
      websocketListeners ! endPointMessage
  }


  override def preStart()={
    log.info("frame handler booting up")
  }
}
