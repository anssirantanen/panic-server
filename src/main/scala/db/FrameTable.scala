package db

import com.outworkers.phantom.Table
import com.outworkers.phantom.jdk8.ZonedDateTime
import com.outworkers.phantom.keys.{PartitionKey, PrimaryKey}
import com.outworkers.phantom.jdk8.indexed._
import com.outworkers.phantom.dsl._
//table mapping Table[cassandratable , case class]
abstract class FrameTable extends Table[FrameTable, models.Frame] with RootConnector{
  object identity extends StringColumn with PartitionKey
  object tag extends OptionalStringColumn
  object level extends IntColumn
  object time extends Col[ZonedDateTime] with PrimaryKey
  object message extends StringColumn with PrimaryKey

}


