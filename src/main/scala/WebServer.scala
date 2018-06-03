import java.util.concurrent.TimeUnit

import incomingFrame.{IncomingFrameGuard, IncomingFrameHandler}
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.ContentNegotiator.Alternative.ContentType
import akka.stream.{ActorMaterializer, Materializer}
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import api.{IncomingFrameApi, MonitorWebsocketApi, Producer}
import scalikejdbc.config.DBs

import scala.concurrent.ExecutionContext

object WebServer  extends App {
  implicit val actorSystem: ActorSystem = ActorSystem("main-system")
  implicit val materializer : ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = actorSystem.dispatcher
  implicit val timeout : Timeout=  Timeout(5, TimeUnit.SECONDS)



  val log = actorSystem.log
  val routes = MonitorWebsocketApi.websocketRoute ~ IncomingFrameApi.incomingFrameApiRoutes
  val routes2 = Producer.producerRoutes()
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
