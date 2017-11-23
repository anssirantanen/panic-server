package db

import com.datastax.driver.core.SocketOptions
import com.outworkers.phantom.dsl._



object Connector {
  val default: CassandraConnection = ContactPoint.local
    .withClusterBuilder(_.withSocketOptions(
      new SocketOptions()
        .setConnectTimeoutMillis(20000)
        .setReadTimeoutMillis(20000)
    )
    ).noHeartbeat().keySpace(
    KeySpace("panic").ifNotExists().`with`(
      replication eqs SimpleStrategy.replication_factor(1)
    )
  )
}
