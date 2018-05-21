name := "AccessControlSystem"

version := "0.1"

scalaVersion := "2.12.5"

lazy val app = (project in file("."))
  .configs(IntegrationTest)
  .enablePlugins(
    sbtdocker.DockerPlugin,
    JavaAppPackaging,
  )
  .settings(Defaults.itSettings)
  .settings(
    libraryDependencies ++= Seq(
      //Akka
      "com.typesafe.akka" %% "akka-actor" % "2.5.12",
      "com.typesafe.akka" %% "akka-stream" % "2.5.11",
      "com.typesafe.akka" %% "akka-http" % "10.1.0",

      //Slick
      "com.typesafe.slick" %% "slick" % "3.2.3",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
      "com.h2database" % "h2" % "1.4.185",
      "org.postgresql" % "postgresql" % "42.1.4",
      "org.liquibase" % "liquibase-core" % "3.5.3",
      "com.mattbertolini" % "liquibase-slf4j" % "2.0.0",

      //Logging
      "org.slf4j" % "slf4j-api" % "1.7.25",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",

      // TestContainers
      "org.testcontainers" % "testcontainers" % "1.7.2" % Test,
      "org.testcontainers" % "postgresql" % "1.7.2" % Test,

      //Tests
      "com.typesafe.slick" %% "slick-testkit" % "3.2.2" % Test,
      "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.12" % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
    )
  )
  .settings(
    dockerfile in docker := {
      val appDir: File = stage.value
      val targetDir = s"/opt/${name.value}"

      new Dockerfile {
        from("java:openjdk-8-jre")
        expose(8080)
        copy(appDir, targetDir)
        workDir(targetDir)
        entryPoint(s"$targetDir/bin/${executableScriptName.value}")
      }
    }
  )