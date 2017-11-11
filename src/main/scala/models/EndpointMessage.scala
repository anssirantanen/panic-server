package models

import io.circe.generic.JsonCodec, io.circe.syntax._

 case class EndpointMessage(name:String,level: Int, message:String)
