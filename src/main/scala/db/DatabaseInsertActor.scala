package db

import akka.actor.{Actor, ActorLogging, Props}
import models.Frame

object DatabaseInsertActor{
  def props = Props(new DatabaseInsertActor)
}
class DatabaseInsertActor extends Actor with db.Connector.connector.Connector with ActorLogging{
  val frameTable = FrameDatabase.frameTable
  override def receive = {
    case fr : Frame => frameTable.store(fr)
  }
}
