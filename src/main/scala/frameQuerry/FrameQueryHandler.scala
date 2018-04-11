package frameQuerry

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.duration._
import db.DatabaseQueryActor
import akka.pattern.ask
import akka.util.Timeout
trait FrameQuery
case class QueryAll() extends FrameQuery

object FrameQueryHandler {
def props = Props(new FrameQueryHandler)
}

class FrameQueryHandler extends Actor with ActorLogging {
  implicit val timeout = Timeout(5 seconds)
  lazy val databaseQueryActor : ActorRef = context.actorOf(DatabaseQueryActor.props)

  override def receive: Receive = {
    case FrameQueryHandler =>
    val frames =  databaseQueryActor ?  "ding"

    case _ =>
  }
}