package models

import java.time.ZonedDateTime

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

trait JsonTypeFormats {
  implicit val ZonedDateTimeFormat : Encoder[ZonedDateTime] with Decoder[ZonedDateTime] = new Encoder[ZonedDateTime] with Decoder[ZonedDateTime] {
    override def apply(a: ZonedDateTime): Json = Encoder.encodeString.apply(a.toOffsetDateTime.toString)
    override def apply(c: HCursor): Result[ZonedDateTime] = Decoder.decodeString.map(s =>ZonedDateTime.parse(s)).apply(c)
  }
}
