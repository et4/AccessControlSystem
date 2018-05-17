name := "AccessControlSystem"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.akka" %% "akka-stream" % "2.5.11",
  "com.typesafe.akka" %% "akka-http" % "10.1.0",
  "com.typesafe.slick" %% "slick" % "3.2.2",
  "com.h2database" % "h2" % "1.4.185",
  "ch.qos.logback" % "logback-classic" % "1.1.2",

  "com.typesafe.slick" %% "slick-testkit" % "3.2.2" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
)
