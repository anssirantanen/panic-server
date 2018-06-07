package db

import akka.event
import akka.event.Logging
import akka.http.scaladsl.server.directives.LogEntry
import core.MainActorSystem
import models.Producer
import scalikejdbc.DBSession
import scalikejdbc._
object ProducerDal {
  private val log = Logging(MainActorSystem.get, this.getClass)
  def create(p:Producer)(implicit dBSession: DBSession):Boolean = {
    try {
      sql"""INSERT INTO producer (name, description) VALUES (${p.name},${p.description})"""
        .update().apply()
      true
    }catch {
      case t:Throwable =>
        log.error(s"failed to create producer  ${p.id} : ${t.getMessage}")
        false
    }
  }
  def update(p:Producer)(implicit dBSession: DBSession):Boolean = {
    try {
      sql"""UPDATE prodcer SET (name = ${p.name}, description =${p.description} WHERE id = ${p.id}"""
        .update().apply()
      true
    }catch{
      case t: Throwable =>
        log.error(s"failed to update producer: ${p.id} : ${t.getMessage}")
        false
    }
  }
  def delete(id:String)(implicit dBSession: DBSession): Boolean = {
    try {
      sql"""DELETE FROM producer WHERE id = CAST($id as UUID)""".update().apply()
        true
    }catch {
      case t:Throwable =>
        log.error(s"failed to delete producer: $id : ${t.getMessage}")
        false
    }
  }
  def get(id:String): Option[Producer] ={
   try {
     sql"""SELECT * FROM producer WHERE id = $id"""
       .stripMargin
       .map(rs => Producer(rs.string("id"),rs.string("name"),rs.string("description"),List()))
       .single().apply()
   }catch {
     case t:Throwable =>
       log.error(s"failed to get procucer: $id : ${t.getMessage}")
       None
   }
  }
  def list():Option[List[Producer]] = {
    try {
     val res =  sql"""SELECT * FROM producer"""
        .stripMargin
        .map(rs => Producer(rs.string("id"),rs.string("name"),rs.string("description"),List()))
        .list.apply()
      Some(res)
    }catch {
      case t :Throwable =>
        log.error(s"failed to get procucer list: ${t.getMessage}")
        None
    }
  }
}
