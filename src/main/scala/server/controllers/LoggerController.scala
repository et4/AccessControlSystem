package server.controllers

import java.sql.Timestamp

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, parameters, path, _}
import akka.http.scaladsl.server.Route
import server.services._

class LoggerController(loggerService: LoggerService) extends Controller {
  val routes: Route = {
    path("getAnomalies") {
      get {
        parameters('fromDateTime.as[Long], 'toDateTime.as[Long], 'times.as[Int]) { (fromDateTime, toDateTime, times) =>
          onSuccess(loggerService.getAnomalies(new Timestamp(fromDateTime), new Timestamp(toDateTime), times)) { value =>
            complete {
              HttpResponse(StatusCodes.OK, entity = HttpEntity(value.mkString(";")))
            }
          }
        }
      }
    }
  } ~
    path("getTimeInside") {
      get {
        parameters('cardId.as[Int], 'fromDateTime.as[Long], 'toDateTime.as[Long]) { (cardId, fromDateTime, toDateTime) =>
          onSuccess(loggerService.getTimeInside(cardId, new Timestamp(fromDateTime), new Timestamp(toDateTime))) { value =>
            complete {
              HttpResponse(StatusCodes.OK, entity = HttpEntity(value.toString))
            }
          }
        }
      }
    } ~
    path("getComeInLogs") {
      get {
        onSuccess(loggerService.getLogs(In).map(_.mkString(";"))) { logs =>
          complete {
            HttpResponse(StatusCodes.OK, entity = HttpEntity(logs))
          }
        }
      }
    } ~
    path("getComeOutLogs") {
      get {
        onSuccess(loggerService.getLogs(Out).map(_.mkString(";"))) { logs =>
          complete {
            HttpResponse(StatusCodes.OK, entity = HttpEntity(logs))
          }
        }
      }
    } ~
    path("getLogs") {
      get {
        parameters('cardId.as[Int]) { (cardId) =>
          onSuccess(loggerService.getLogsByCard(cardId, All).map(_.mkString(";"))) { logs =>
            complete {
              HttpResponse(StatusCodes.OK, entity = HttpEntity(logs))
            }
          }
        }
      } ~ get {
        parameters('cardId.as[Int], 'event.as[String]) { (cardId, event) =>
          val ev = event.toUpperCase match {
            case "IN" => In
            case "OUT" => Out
            case "ALL" => All
          }
          onSuccess(loggerService.getLogsByCard(cardId, ev).map(_.mkString(";"))) { logs =>
            complete {
              HttpResponse(StatusCodes.OK, entity = HttpEntity(logs))
            }
          }
        }
      }
    }
}
