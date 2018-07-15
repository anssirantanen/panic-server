import db.ProducerDalTransaction
import mocModels.ProducerMocs
import models.ProducerModel
import org.scalatest.{Matchers, WordSpec}
import producer.ProducerServiceImpl
import org.scalatest.TryValues._
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global


class ProducerServiceSpec  extends WordSpec with Matchers {
  val service = new ProducerServiceImpl(new MockProducerTransaction)
  "get" should {
    "be success none with random id" in {
      service.get("str")
      .map{
        rs => rs shouldEqual Success(Right(None))
      }
    }
    "be failure on invaliduuid" in {
      service.get("invaliduuid")
        .map{rs =>
        rs shouldEqual Failure
      }
    }
    "return result" in {
      service.get("id").map{res =>
        res shouldEqual Future(Success(Some(ProducerMocs.mockWithId)))
      }
    }
  }
  "create" should {
    "add id" in {
      service.create(ProducerMocs.mockWithoutId).map{
        case Right(p) => p.id.isDefined
        case Left(err) => false
      }
    }
  }
  "delete" should {
    "be right unit" in {
      service.delete("id").map {
      case Right(unit) => true
      case Left(err) => false
      }
    }
  }
  "list" should {
    "return producer list" in {
      service.list().map{
        case Right(list) => list shouldEqual List(ProducerMocs.mockWithId)
        case Left(err) => false
      }
    }
  }
  "update" should {
    "return unit" in {
      service.update(ProducerMocs.mockWithId).map{
        case Right(unit) => true
        case Left(err) => false
      }
    }
  }
}

class MockProducerTransaction extends ProducerDalTransaction {
  override def get(id: String): Future[Try[Option[ProducerModel]]] = {
    id match {
      case "invaliduuid" => Future(Failure[Option[ProducerModel]](new Exception("error")))
      case "id" => Future(Try(Some(ProducerMocs.mockWithId)))
      case _ =>     Future(Try(None))

    }
  }

  override def create(p: ProducerModel): Future[Try[ProducerModel]] = {
    Future(Try(p.copy(id =Some("withId"))))
  }

  override def delete(id: String): Future[Try[Unit]] = {
    Future.successful(Success[Unit]())
  }

  override def list(): Future[Try[List[ProducerModel]]] = {
    Future.successful(Success[List[ProducerModel]](List(ProducerMocs.mockWithId)))
  }

  override def update(producerModel: ProducerModel): Future[Try[Unit]] = {
    Future.successful(Success[Unit]())
  }
}