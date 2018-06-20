package core
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
trait ServerError{
  val msg:String
}
object ServerError{

  def toResponse(err: ServerError): StandardRoute = {

    err match {
      case i: InternalServerError => complete(StatusCodes.InternalServerError, i.msg)
    }
  }
}

case class InternalServerError(msg:String = "") extends ServerError
case class NotFound(msg:String = "")extends ServerError