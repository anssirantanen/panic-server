import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.ContentNegotiator.Alternative.ContentType
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext
import scala.io.StdIn


object WebServer {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem: ActorSystem = ActorSystem("baseActorSystem")
    implicit val materializer : ActorMaterializer = ActorMaterializer()

    implicit val executionContext: ExecutionContext = actorSystem.dispatcher

    val route =
      path("root"){
        get{
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`,"root"))
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done
  }
}
