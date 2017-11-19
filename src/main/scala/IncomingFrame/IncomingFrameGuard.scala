package IncomingFrame

import akka.actor.{Actor, ActorRef, Props}


object IncomingFrameGuard {
  def props() : Props = Props(new IncomingFrameGuard)
}

class IncomingFrameGuard  extends  Actor{
  val incomingFrameHandler: ActorRef = context.actorOf(IncomingFrameHandler.props(),"IncomingFrameHandler")
  override def receive: Receive = {
    case _ =>
  }
}
