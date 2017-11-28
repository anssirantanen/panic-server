package db
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database

import db.Connector._

class FrameDatabase(override val connector: CassandraConnection) extends Database[FrameDatabase](connector){
  object frameTable extends FrameTable with connector.Connector
}

//connector from db.connector object
object FrameDatabase extends FrameDatabase(connector )