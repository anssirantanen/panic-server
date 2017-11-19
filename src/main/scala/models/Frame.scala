package models

import java.time.ZonedDateTime

case class Frame(identity: String, name:Option[String], level: Int, time : ZonedDateTime, message:String)
