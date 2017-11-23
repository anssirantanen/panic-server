package models

import java.time.ZonedDateTime

case class Frame(identity: String, tag: Option[String], level: Int, time : ZonedDateTime, message:String)

/*
CREATE TABLE frame (
identity uuid,
tag text,
level Int,
message text,
time timestamp,
PRIMARY KEY (identity, message, time))
                */