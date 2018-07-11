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
  def create(p:ProducerModel)(implicit db: DBSession):Try[ProducerModel] = {
    Try {
      val id =
        sql"""INSERT INTO producer (name, description) VALUES (${p.name},${p.description}) RETURNING id"""
          .stripMargin.map(rs => rs.string("id")).single().apply()
      p.copy(id = id)
    }
  }
  def update(p:ProducerModel)(implicit dBSession: DBSession):Try[Unit] = {
    Try{
      sql"""UPDATE prodcer SET (name = ${p.name}, description =${p.description} WHERE id = ${p.id}"""
        .update().apply()
    }
  }
  def delete(id:String)(implicit dBSession: DBSession): Try[Unit] = {
    Try {sql"""DELETE FROM producer WHERE id = CAST($id as UUID)""".update().apply()
    }
  }

  def get(id:String)(implicit se: DBSession): Try[Option[ProducerModel]] ={
    Try {
      sql"""SELECT * FROM producer WHERE id = $id"""
        .stripMargin
        .map(rs => ProducerModel(rs.stringOpt("id"), rs.string("name"), rs.string("description"), List()))
        .single().apply()
    }
  }

  def list()(implicit dBSession: DBSession):Try[List[ProducerModel]] = {
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