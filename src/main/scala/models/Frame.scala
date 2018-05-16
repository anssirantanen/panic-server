package models

import java.time.ZonedDateTime

trait FrameComponents{
  val identity: String
  val level : Int
  val Message : String
}
case class Frame(identity: String, tag: List[String], level: Int, time : ZonedDateTime, message:String)
case class UnTimedFrame(override val identity: String, override val level: Int, override val Message: String) extends FrameComponents
/*
CREATE TABLE frame (
identity uuid,
tag text,
level Int,
message text,
time timestamp,
PRIMARY KEY (identity, message, time))
                */