package incomingFrame

import incomingFrame.IncomingFrameHandler.TextMessage
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import db.DatabaseInsertActor
import models.Frame
import uiComponents.WebsocketListeners


object IncomingFrameHandler{
  case class TextMessage(textMessage: String)
  def props : Props = Props(new IncomingFrameHandler)
}

class IncomingFrameHandler extends Actor with ActorLogging{
  val websocketListeners: ActorRef = context.actorOf(WebsocketListeners.props, "monitor-websockets-actor")
  val databaseInsertActor: ActorRef = context.actorOf(DatabaseInsertActor.props)
  override def receive: Receive = {
    case message : TextMessage =>
      websocketListeners ! message
    case endPointMessage : Frame =>
      databaseInsertActor ! Frame
      websocketListeners ! endPointMessage
  }
}
