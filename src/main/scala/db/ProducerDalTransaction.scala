package db

import models.ProducerModel
import scalikejdbc.{DBSession, NamedDB}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try


class ProducerDalTransaction(dal : ProducerDal) extends  PanicTransaction {
  def get(id : String): Future[Try[Option[ProducerModel]]] = {
    val getCurried  = dal.get(id)(_)
    transaction(getCurried)
  }
  def create(p:ProducerModel): Future[Try[ProducerModel]] = {
    val producerCurried = dal.create(p)(_)
    transaction(producerCurried)
  }
  def delete(id:String): Future[Try[Unit]] = {
    val delCurried = dal.delete(id)(_)
    transaction(delCurried)
  }
  def list() : Future[Try[List[ProducerModel]]] = {
    val listCurried: DBSession => Try[List[ProducerModel]] = dal.list()
    transaction(listCurried)
  }

  override implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
}
