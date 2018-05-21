package server.controllers

import _root_.server.services.CardService
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.Await

class TurnstileController(cardManager: CardService) extends Controller {
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
}