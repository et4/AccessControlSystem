package server.controllers

import java.sql.Timestamp
import java.time.Instant

import _root_.server.services.{CardService, LoggerService}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class TurnstileController(cardManager: CardService, loggerService: LoggerService) extends Controller {
  val routes: Route = {
    path("action") {
      get {
        parameters('cardId.as[String], 'date.as[String], 'event.as[String]) { (cardId, date, event) =>
          onSuccess(cardManager.hasAccess(cardId.toInt)) { value =>
            complete {
              loggerService.log(cardId.toInt, Timestamp.from(Instant.ofEpochMilli(date.toLong)), event, value)
              if (value)
                HttpResponse(StatusCodes.OK)
              else
                HttpResponse(StatusCodes.Forbidden)
            }
          }
        }
      }
    }
  } ~ path("hasAccess") {
    get {
      parameters('cardId.as[String]) { cardId =>
        onSuccess(cardManager.hasAccess(cardId.toInt)) { value =>
          complete {
            HttpResponse(StatusCodes.OK, entity = value.toString)
          }
        }
      }
    }
  }
}