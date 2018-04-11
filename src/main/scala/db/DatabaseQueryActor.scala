package db

import akka.actor.{Actor, Props}

object DatabaseQueryActor {
 def props = Props(new DatabaseQueryActor)
}

class DatabaseQueryActor extends Actor with db.Connector.connector.Connector {
  val frameTable = FrameDatabase.frameTable

  override def receive: Receive = {
    case _ => sender() ! frameTable.select.all()
  }
}
