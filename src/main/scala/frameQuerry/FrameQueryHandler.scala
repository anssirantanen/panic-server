package frameQuerry

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.duration._

import akka.pattern.ask
import akka.util.Timeout
trait FrameQuery
case class QueryAll() extends FrameQuery

object FrameQueryHandler {
def props = Props(new FrameQueryHandler)
 def hardAddress = "/user/IncomingFrameGuard/IncomingFrameHandler/monitor-websockets-actor"
}

class FrameQueryHandler extends Actor with ActorLogging {
  implicit val timeout = Timeout(5 seconds)

  override def receive: Receive = {
    case FrameQueryHandler =>
    case _ =>
  }
}
