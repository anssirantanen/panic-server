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
              case Left(err) => core.ServerError.toResponse(err)
              case Right(np) => complete(np)
            }
          } ~
            complete(BadRequest, "Not a correct producer")
        } ~
        complete(NotFound)
      } ~
      path(IntNumber){ id =>
        get{
          {
            onSuccess(ps.get(id)){
              case Left(err) => core.ServerError.toResponse(err)
              case Right(np) =>  np.map(complete(_)).getOrElse(complete(NotFound))
            }
          }
        }~
          put{
            entity(as[ProducerModel]){ producer =>
              onSuccess(ps.update(producer)){
                case Left(err) => core.ServerError.toResponse(err)
                case Right(np) => complete(np)
              }
            }~
            complete(BadRequest)
          } ~
          delete{
            onSuccess(ps.delete(id)){
              case Left(err) => core.ServerError.toResponse(err)
              case Right(np) => if(np) complete() else complete(InternalServerError)
            }
          } ~
        complete(NotFound)
      }
    }
  }
}
