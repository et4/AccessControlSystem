package server.controllers

import _root_.server.services.CardService
import akka.http.scaladsl.model.StatusCodes.Success
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}

import scala.concurrent.Await
import scala.util.Try

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
  } ~ path("hasAccess") {
    get {
      parameters('cardId.as[String]) { (cardId) =>
        complete {
          HttpResponse(StatusCodes.OK,
            entity = HttpEntity(Await.result(cardManager.hasAccess(cardId.toInt), timeout).toString))
        }
      }
    }
  }
}