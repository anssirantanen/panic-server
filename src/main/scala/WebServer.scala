import java.util.concurrent.TimeUnit

import incomingFrame.{IncomingFrameGuard, IncomingFrameHandler}
import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.ContentNegotiator.Alternative.ContentType
import akka.stream.{ActorMaterializer, Materializer}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.LogEntry
import akka.util.Timeout
import api.{IncomingFrameApi, MonitorWebsocketApi, ProducerRoutes}
import core.MainActorSystem
import db.ProducerDalImpl
import producer.ProducerServiceImpl
import scalikejdbc.config.DBs

import scala.concurrent.ExecutionContext

object WebServer  extends App {
  implicit val actorSystem: ActorSystem =MainActorSystem.get
  implicit val materializer : ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = actorSystem.dispatcher
  implicit val timeout : Timeout=  Timeout(5, TimeUnit.SECONDS)


  val log = Logging(MainActorSystem.get, this.getClass)
  val routes = MonitorWebsocketApi.websocketRoute ~ IncomingFrameApi.incomingFrameApiRoutes
  val mock = new ProducerServiceImpl(new ProducerDalImpl())
  val routes2 = ProducerRoutes.producerRoutes(mock)
  val bindingFuture = Http().bindAndHandle(routes2, "localhost", 9000)


  DBs.setup('panic)

  bindingFuture
    .map(_.localAddress)
    .map(addr => s"Bound to $addr")
    .foreach(log.info)

    initializeGuardActors()


  def initializeGuardActors()={
    IncomingFrameGuard.instance
  }
}
