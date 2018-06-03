package api

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{PathMatchers, Route}
import core.MainActorSystem

object Producer {
  val actorSystem : ActorSystem = MainActorSystem.get

  def producerRoutes():Route = {
    pathPrefix("producer"){
      pathEndOrSingleSlash {
        complete("base")
      } ~
      path("producer" / IntNumber){ id =>
        pathEndOrSingleSlash{
          complete("num")
        }
      }
    }
  }
}
