package server.controllers

import java.time.Instant

import akka.actor.ActorSystem
import akka.http.scaladsl.{Http, server}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import _root_.server.services.{CardManagerService, GroupManagerService}
import akka.http.scaladsl.server.Route
import slick.jdbc.H2Profile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}


object TurnstileController {

  case class OpenTurnstileRequest(id: String, date: Instant, event: String)

}

class TurnstileController(cardManager: CardManagerService)(implicit val db: H2Profile.backend.Database)
  extends Controller {

  //  val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
  //    Http().bind(interface = "localhost", port = 8182)

  val routes: Route = {
    path("action") {
      get {
        parameters('cardId.as[String], 'date.as[String], 'event.as[String]) { (cardId, date, event) =>
          complete {
            if (Await.result(cardManager.hasAccess(cardId.toInt), timeout))
              HttpResponse(StatusCodes.OK)
            else
              HttpResponse(StatusCodes.Forbidden)
          }
        }
      }
    }
  }

  //  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8182)
  //
  //  println("Server online at http://localhost:8080/")
  //  println("Press RETURN to stop...")
  //  StdIn.readLine() // let it run until user presses return
  //  bindingFuture
  //    .flatMap(_.unbind()) // trigger unbinding from the port
  //    .onComplete(_ => system.terminate()) // and shutdown when done
}