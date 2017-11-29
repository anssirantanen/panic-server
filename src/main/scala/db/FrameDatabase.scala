package db
import com.outworkers.phantom.dsl._
import com.outworkers.phantom.connectors

class FrameDatabase(override val connector: CassandraConnection) extends Database[FrameDatabase](connector){
  object frameTable extends FrameTable with Connector
}

//connector from db.connector object
object FrameDatabase extends FrameDatabase(Connector.connector)