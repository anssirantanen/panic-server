import java.util.concurrent.TimeUnit

import incomingFrame.{IncomingFrameGuard, IncomingFrameHandler}
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.ContentNegotiator.Alternative.ContentType
import akka.stream.{ActorMaterializer, Materializer}
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import api.{IncomingFrameApi, MonitorWebsocketApi}

import scala.concurrent.ExecutionContext
import scala.io.StdIn


object WebServer  extends App with IncomingFrameApi with MonitorWebsocketApi{
  implicit val actorSystem: ActorSystem = ActorSystem("main-system")
  implicit val materializer : ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = actorSystem.dispatcher
  implicit val timeout : Timeout=  Timeout(5, TimeUnit.SECONDS)

  val log = actorSystem.log
  val frameGuard = actorSystem.actorOf(IncomingFrameGuard.props(), "IncomingFrameGuard")
  val routes = websocketRoute ~ infoFrameApiRoutes
  val bindingFuture = Http().bindAndHandle(routes, "localhost", 9000)

  bindingFuture
    .map(_.localAddress)
    .map(addr => s"Bound to $addr")
    .foreach(log.info)
}
