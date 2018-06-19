package producer

import core.{InternalServerError, ServerError}
import models.ProducerModel

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait ProducerService {
  def create(producer: ProducerModel): Future[Either[ServerError, ProducerModel]]
  def update(producer: ProducerModel): Future[Either[ServerError, ProducerModel]]
  def get(id:Int): Future[Either[ServerError, Option[ProducerModel]]]
  def delete(id: Int): Future[Either[ServerError,Boolean]]
  def list(): Future[Either[ServerError,List[ProducerModel]]]
}

class ProducerServiceImpl extends ProducerService{
  override def create(producer: ProducerModel): Future[Either[ServerError, ProducerModel]] = {
    Future.successful(Left(InternalServerError()))
  }

  override def update(producer: ProducerModel): Future[Either[ServerError, ProducerModel]] = {
    Future.successful(Left(InternalServerError()))
  }

  override def get(id: Int): Future[Either[ServerError, Option[ProducerModel]]] = {
    Future.successful(Left(InternalServerError()))
  }

  override def delete(id: Int): Future[Either[ServerError, Boolean]] = {
    Future.successful(Left(InternalServerError()))
  }

  override def list(): Future[Either[ServerError, List[ProducerModel]]] = {
    Future.successful(Left(InternalServerError()))
  }
}
