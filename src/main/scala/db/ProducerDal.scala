package db

import akka.event
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.directives.LogEntry
import core.MainActorSystem
import models.ProducerModel
import scalikejdbc.GeneralizedTypeConstraintsForWithExtractor.=:=
import scalikejdbc.{DBSession, WithExtractor, _}

import scala.util.Try
trait ProducerDal {
  def create(p:ProducerModel)( db: DBSession):Try[ProducerModel] = {
    implicit val s: DBSession = db
    Try {
      val id =
        sql"""INSERT INTO producer (name, description) VALUES (${p.name},${p.description}) RETURNING id"""
          .stripMargin.map(rs => rs.string("id")).single().apply()
      p.copy(id = id)
    }
  }
  def update(p:ProducerModel)( dBSession: DBSession):Try[Unit] = {
    implicit val s = dBSession
    Try{
      sql"""UPDATE prodcer SET (name = ${p.name}, description =${p.description} WHERE id = ${p.id}"""
        .update().apply()
    }
  }
  def delete(id:String)(dBSession: DBSession): Try[Unit] = {
    implicit val session: DBSession = dBSession
    Try {sql"""DELETE FROM producer WHERE id = CAST($id as UUID)""".update().apply()
    }
  }

  def get(id:String)(se: DBSession): Try[Option[ProducerModel]] ={
    implicit val session: DBSession = se
    Try {
      sql"""SELECT * FROM producer WHERE id = $id"""
        .stripMargin
        .map(rs => ProducerModel(rs.stringOpt("id"), rs.string("name"), rs.string("description"), List()))
        .single().apply()
    }
  }

  def list()(dBSession: DBSession):Try[List[ProducerModel]] = {
    implicit  val s: DBSession = dBSession
    Try{
     lazy val res =  sql"""SELECT * FROM producer"""
        .stripMargin
        .map(rs => ProducerModel(rs.stringOpt("id"),rs.string("name"),rs.string("description"),List()))
        .list.apply()
     res
    }
  }
}
class ProducerDalImpl extends ProducerDal {

}