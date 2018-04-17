package incomingFrame

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import core.MainActorSystem


object IncomingFrameGuard {
  val instance = MainActorSystem.get.actorOf(props(),"IncomingFrameGuard")
  def props() : Props = Props(new IncomingFrameGuard)
}

class IncomingFrameGuard  extends  Actor with ActorLogging{
  log.info("booting IncomingFrameGuard")
  val incomingFrameHandler: ActorRef = context.actorOf(IncomingFrameHandler.props,"IncomingFrameHandler")
  override def receive: Receive = {
    case _ =>
  }
}
