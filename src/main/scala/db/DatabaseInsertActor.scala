package db

import akka.actor.{Actor, Props}
import models.Frame

object DatabaseInsertActor{
  def props = Props(new DatabaseInsertActor)
}
class DatabaseInsertActor extends Actor with db.Connector.connector.Connector {
  val frameTable = FrameDatabase.frameTable
  override def receive = {
    case fr : Frame => frameTable.store(fr)
  }
}
