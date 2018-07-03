package db

import scalikejdbc.{DBSession, NamedDB}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

trait PanicTransaction {
  implicit val ec : ExecutionContext
  def transaction[T](f: DBSession => T):Future[T] = {
    NamedDB('panic) futureLocalTx { implicit session =>
      Future{
        f(session)
      }
    }
  }
}
