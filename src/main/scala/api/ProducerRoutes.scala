package api

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{PathMatchers, Route}
import api.IncomingFrameApi.incomingFrameHandler
import core.MainActorSystem
import models.ProducerModel
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import akka.http.scaladsl.model.StatusCodes._
import producer.ProducerService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ProducerRoutes {
  val actorSystem : ActorSystem = MainActorSystem.get

  def producerRoutes(ps: ProducerService):Route = {
    pathPrefix("producer"){
      pathEndOrSingleSlash {
        post {
          entity(as[ProducerModel]){ producer=>
            onSuccess(ps.create(producer)) {
              case Left(err) => complete("")
              case Right(np) => complete("")
            }
          } ~
            complete(BadRequest, "Not a correct producer")
        } ~
        complete(BadRequest, "Not a producer.")
      } ~
      path(IntNumber){ id =>
        get{
          entity(as[ProducerModel]){ producer =>
            complete("num"+ id)
          }
        }~
          put{
            entity(as[ProducerModel]){ producer =>
              complete("update")
            }~
            complete(BadRequest)
          } ~
        complete(NotFound)
      }
    }
  }
}
