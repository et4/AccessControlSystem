package client.services

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.ExecutionContextExecutor

trait Service {
  val timeout: FiniteDuration = 10000.millis

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
}
