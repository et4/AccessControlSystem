package server.controllers

import java.sql.Timestamp

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
              value match {
                case Some(v) => {
                  loggerService.log(cardId.toInt, new Timestamp(date.toLong), event, v)
                  if (v)
                    HttpResponse(StatusCodes.OK)
                  else
                    HttpResponse(StatusCodes.Forbidden)
                }
                case None => HttpResponse(StatusCodes.NotFound)
              }
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
            value match {
              case Some(v) => HttpResponse(StatusCodes.OK, entity = v.toString)
              case None => HttpResponse(StatusCodes.NotFound)
            }
          }
        }
      }
    }
  }
}