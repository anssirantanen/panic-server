package producer

import core.{InternalServerError, ServerError}
import db.ProducerDal
import models.ProducerModel
import scalikejdbc.NamedDB

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait ProducerService {
  def create(producer: ProducerModel): Future[Either[ServerError, ProducerModel]]
  def update(producer: ProducerModel): Future[Either[ServerError, ProducerModel]]
  def get(id:String): Future[Either[ServerError, Option[ProducerModel]]]
  def delete(id: String): Future[Either[ServerError,Boolean]]
  def list(): Future[Either[ServerError,List[ProducerModel]]]
}

class ProducerServiceImpl(dal: ProducerDal) extends ProducerService {
  override def create(producer: ProducerModel): Future[Either[ServerError, ProducerModel]] = {
    NamedDB('panic) futureLocalTx { implicit  session =>
      Future{
        dal.create(producer) match {
          case Some(p) => Right(p)
          case None => Left(InternalServerError)
        }
      }
    }
  }

  override def update(producer: ProducerModel): Future[Either[ServerError, ProducerModel]] = {
    Future.successful(Left(InternalServerError()))
  }

  override def get(id: String): Future[Either[ServerError, Option[ProducerModel]]] = {
    Future.successful(Left(InternalServerError()))
  }

  override def delete(id: String): Future[Either[ServerError, Boolean]] = {
    Future.successful(Left(InternalServerError()))
  }

  override def list(): Future[Either[ServerError, List[ProducerModel]]] = {
    Future.successful(Left(InternalServerError()))
  }
}
