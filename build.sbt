name := "panic"

version := "0.1"

scalaVersion := "2.12.4"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.6",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.6" % Test,
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test,

  "de.heikoseeberger" %% "akka-http-circe" % "1.19.0-M3",
  "io.circe"          %% "circe-core"        % "0.9.0-M2",
  "io.circe"          %% "circe-generic"     % "0.9.0-M2",
  "io.circe"          %% "circe-parser"     % "0.9.0-M2",

)
