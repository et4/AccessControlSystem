package server.controllers

import java.time.Instant

import _root_.server.services.CardService
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.Await


object TurnstileController {

  case class OpenTurnstileRequest(id: String, date: Instant, event: String)

}

class TurnstileController(cardManager: CardService)
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