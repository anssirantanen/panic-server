package db

import com.datastax.driver.core.SocketOptions
import com.outworkers.phantom.dsl._



object Connector {
 /* val connector: CassandraConnection = ContactPoint.local
    .withClusterBuilder(_.withSocketOptions(
      new SocketOptions()
        .setConnectTimeoutMillis(20000)
        .setReadTimeoutMillis(20000)

    )
    ).noHeartbeat().keySpace(
    KeySpace("panic").ifNotExists().`with`(
      replication eqs SimpleStrategy.replication_factor(1)
    )
  )*/
  val connector : CassandraConnection = ContactPoint("localhost",9042).keySpace("panic")
}
