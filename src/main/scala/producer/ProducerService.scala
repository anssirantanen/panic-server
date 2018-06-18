package producer

import core.ServerError
import models.ProducerModel

import scala.concurrent.Future

trait ProducerService {
  def create(producer: ProducerModel): Future[Either[ServerError, ProducerModel]]
  def update(producer: ProducerModel): Future[Either[ServerError, ProducerModel]]
  def get(id:Int): Future[Future[Either[ServerError, Option[ProducerModel]]]]
  def delete(id: Int): Future[Either[ServerError,Boolean]]
  def list(): Future[Either[ServerError,List[ProducerModel]]]
}
