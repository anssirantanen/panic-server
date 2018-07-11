import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpResponse, MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import api.ProducerRoutes
import core.{InternalServerError, NotFound, ServerError}
import models.ProducerModel
import org.scalatest.{Matchers, WordSpec}
import producer.ProducerService
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.Json

import scala.concurrent.Future

private object Mocs{
  val mockP1 = ProducerModel(None,"name1", "desc",List())
}

class ProducerRouteSpec extends WordSpec with Matchers with ScalatestRouteTest {
  val mockService = new MockProducerService
  val routes = ProducerRoutes.producerRoutes(mockService)
  val mockEntity = Marshal(Mocs.mockP1).to[MessageEntity]

  "Producer routes" should {
    "get" should {
      "Empty get 404" in {
        Get("/producer") ~> routes ~> check {
          status shouldEqual StatusCodes.NotFound
        }
      }
      "id:1 get returns ok" in {
        Get("/producer/1") ~> routes ~> check {

          status shouldEqual StatusCodes.OK
        }
      }
      "id:2 get returns not found" in {
        Get("/producer/2") ~> routes ~> check {
          status shouldEqual StatusCodes.NotFound
        }
      }
      "negative id throws error" in {
        Get("/producer/3") ~> routes ~> check {
          status shouldEqual StatusCodes.InternalServerError
        }
      }
    }
    "post" should {
      "response ok " in {
        Post("/producer/", Marshal(Mocs.mockP1).to[MessageEntity]) ~> routes ~> check {
          status shouldEqual StatusCodes.OK
        }
      }
      "add an id in post" in {
        Post("/producer/", Marshal(Mocs.mockP1).to[MessageEntity]) ~> routes ~> check {
          responseAs[ProducerModel] shouldEqual Mocs.mockP1.copy(id = Some("id"))
        }
      }
    }
    "put" should {
      "return ok response" in {
        Put("/producer/id", Marshal(Mocs.mockP1.copy(id = Some("id"))).to[MessageEntity]) ~> routes ~> check {
          responseAs[ProducerModel] shouldEqual Mocs.mockP1.copy(id = Some("id"))
        }
      }
      "return bad request on string message" in {
        Put("/producer/id", Marshal("str message").to[MessageEntity]) ~> routes ~> check {
          status shouldEqual StatusCodes.BadRequest
        }
      }
    }
  }
}
class MockProducerService extends ProducerService {
  override def create(producer: ProducerModel): Future[Either[ServerError, ProducerModel]] = {
    Future.successful(Right(producer.copy(id = Some("id"))))
  }
  override def update(producer: ProducerModel): Future[Either[ServerError, ProducerModel]] = {
    Future.successful(Right(producer))
  }
  override def get(id: String): Future[Either[ServerError, Option[ProducerModel]]] = {
    if(id=="1"){
      Future.successful(Right(Some(Mocs.mockP1)))
    } else if(id=="2"){
      Future.successful(Right(None))
    }else{
      Future.successful(Left(InternalServerError()))
    }
  }
  override def delete(id: String): Future[Either[ServerError, Unit]] = {
    Future.successful(Left(InternalServerError()))
  }
  override def list(): Future[Either[ServerError, List[ProducerModel]]] = {
    Future.successful(Left(InternalServerError()))
  }
}