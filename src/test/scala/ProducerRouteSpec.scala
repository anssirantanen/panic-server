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
import mocModels.ProducerMocs

import scala.concurrent.Future


class ProducerRouteSpec extends WordSpec with Matchers with ScalatestRouteTest {
  val mockService = new MockProducerService
  private val routes = ProducerRoutes.producerRoutes(mockService)

  "Producer routes" should {
    "get" should {
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
        Post("/producer/", Marshal(ProducerMocs.mockWithoutId).to[MessageEntity]) ~> routes ~> check {
          status shouldEqual StatusCodes.OK
        }
      }
      "add an id in post" in {
        Post("/producer/", Marshal(ProducerMocs.mockWithoutId).to[MessageEntity]) ~> routes ~> check {
          responseAs[ProducerModel] shouldEqual ProducerMocs.mockWithoutId.copy(id = Some("id"))
        }
      }
    }
    "put" should {
      "return ok response" in {
        Put("/producer/id", Marshal(ProducerMocs.mockWithoutId.copy(id = Some("id"))).to[MessageEntity]) ~> routes ~> check {
          responseAs[ProducerModel] shouldEqual ProducerMocs.mockWithoutId.copy(id = Some("id"))
        }
      }
      "return bad request on string message" in {
        Put("/producer/id", Marshal("str message").to[MessageEntity]) ~> routes ~> check {
          status shouldEqual StatusCodes.BadRequest
        }
      }
    }
    "delete" should {
      "return ok on success" in {
        Delete("/producer/id") ~> routes ~> check{
          status shouldEqual StatusCodes.OK
        }
      }
    }
    "list " should {
      "return ok" in {
        Get("/producer/") ~> routes ~> check{
          status shouldEqual StatusCodes.OK
        }
      }
      "should return list of 1" in {
        Get("/producer/") ~> routes ~> check{
          responseAs[List[ProducerModel]] shouldEqual List(ProducerMocs.mockWithoutId)
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
      Future.successful(Right(Some(ProducerMocs.mockWithoutId)))
    } else if(id=="2"){
      Future.successful(Right(None))
    }else{
      Future.successful(Left(InternalServerError()))
    }
  }
  override def delete(id: String): Future[Either[ServerError, Unit]] = {
    Future.successful(Right())
  }
  override def list(): Future[Either[ServerError, List[ProducerModel]]] = {
    Future.successful(Right(List(ProducerMocs.mockWithoutId)))
  }
}