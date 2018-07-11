package producer

import akka.event.Logging
import akka.parboiled2.RuleTrace.Fail
import core.{InternalServerError, MainActorSystem, NotFound, ServerError}
import db.{ProducerDal, ProducerDalTransaction}
import models.ProducerModel
import scalikejdbc.NamedDB

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

trait ProducerService {
  def create(producer: ProducerModel): Future[Either[ServerError, ProducerModel]]
  def update(producer: ProducerModel): Future[Either[ServerError, ProducerModel]]
  def get(id:String): Future[Either[ServerError, Option[ProducerModel]]]
  def delete(id: String): Future[Either[ServerError,Unit]]
  def list(): Future[Either[ServerError,List[ProducerModel]]]
}

class ProducerServiceImpl(dal: ProducerDalTransaction) extends ProducerService {
  val log = Logging(MainActorSystem.get, this.getClass)
  override def create(producer: ProducerModel): Future[Either[ServerError, ProducerModel]] = {
    dal.create(producer).map {
      case Success(model) => Right(model)
      case Failure(cause) =>
        log.error(s"Failed to new create producer: ${cause.getMessage}")
        Left(InternalServerError(cause.getMessage))
    }
  }

  override def update(producer: ProducerModel): Future[Either[ServerError, ProducerModel]] = {
    dal.update(producer).map{
      case Success(u) => Right(producer)
      case Failure(cause) =>
        log.error(s"failed to update producer id : ${producer.id} :  ${cause.getMessage}")
        Left(InternalServerError(cause.getMessage))
    }
  }

  override def get(id: String): Future[Either[ServerError, Option[ProducerModel]]] = {
    dal.get(id).map{
      case Success(mod) => Right(mod)
      case Failure(cause) =>
        log.error(s"Failed to get producer id: $id: ${cause.getMessage}")
        Left(InternalServerError(cause.getMessage))
    }
  }

  override def delete(id: String): Future[Either[ServerError, Unit]] = {
    dal.delete(id).map{
      case Success(b) => Right(b)
      case Failure(cause) =>
        log.error(s"failed to delete producer : $id : ")
        Left(InternalServerError(cause.getMessage))
    }
  }

  override def list(): Future[Either[ServerError, List[ProducerModel]]] = {
      dal.list().map{
      case Success(list) => Right(list)
      case Failure(cause) =>
        log.error(s"failed to list producers: ${cause.getMessage}")
        Left(InternalServerError(cause.getMessage))
    }
  }
}
