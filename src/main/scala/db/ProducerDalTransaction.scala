package db

import models.ProducerModel
import scalikejdbc.{DBSession, NamedDB}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

trait ProducerDalTransaction {
  def get(id : String): Future[Try[Option[ProducerModel]]]
  def create(p:ProducerModel): Future[Try[ProducerModel]]
  def delete(id:String): Future[Try[Unit]]
  def list() : Future[Try[List[ProducerModel]]]
  def update(producerModel: ProducerModel): Future[Try[Unit]]
}

class ProducerDalTransactionImpl(dal : ProducerDal) extends ProducerDalTransaction {
  def get(id : String): Future[Try[Option[ProducerModel]]] = {
    NamedDB('panic) futureLocalTx { implicit session =>
      Future{
        dal.get(id)
      }
    }
  }
  def create(p:ProducerModel): Future[Try[ProducerModel]] = {
    NamedDB('panic) futureLocalTx { implicit session =>
      Future {
        dal.create(p)
      }
    }
  }
  def delete(id:String): Future[Try[Unit]] = {
    NamedDB('panic) futureLocalTx { implicit session =>
      Future {
        dal.delete(id)
      }
    }
  }
  def list() : Future[Try[List[ProducerModel]]] = {
    NamedDB('panic) futureLocalTx { implicit session =>
      Future {
        dal.list()
      }
    }
  }
  def update(producerModel: ProducerModel): Future[Try[Unit]] = {
    NamedDB('panic) futureLocalTx { implicit session =>
      Future {
        dal.update(producerModel)
      }
    }
  }
}
