import IncomingMessage.IncomingMessageHandler
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.ContentNegotiator.Alternative.ContentType
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import api.IncomingMessageApi

import scala.concurrent.ExecutionContext
import scala.io.StdIn


object WebServer  extends IncomingMessageApi{
  def main(args: Array[String]): Unit = {

    implicit val materializer : ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher

    val bindingFuture = Http().bindAndHandle(infoMessageApiRoutes, "localhost", 9000)
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done
  }


}
