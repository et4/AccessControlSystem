package server.controllers

import java.sql.Timestamp

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, parameters, path, _}
import akka.http.scaladsl.server.Route
import server.services._

import scala.concurrent.{Await, Future}

class LoggerController(loggerService: LoggerService) extends Controller {
  val routes: Route = {
    path("getAnomalies") {
      get {
        parameters('fromDateTime.as[Long], 'toDateTime.as[Long], 'times.as[Int]) { (fromDateTime, toDateTime, times) =>
          complete {
            loggerService.getAnomalies(new Timestamp(fromDateTime), new Timestamp(toDateTime), times)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    }
  } ~
    path("getTimeInside") {
      get {
        parameters('cardId.as[Int], 'fromDateTime.as[Long], 'toDateTime.as[Long]) { (cardId, fromDateTime, toDateTime) =>
          complete {
            loggerService.getTimeInside(cardId, new Timestamp(fromDateTime), new Timestamp(toDateTime))
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("getComeInLogs") {
      get {
        complete {
          val ids = Await.result(loggerService.getLogs(In), timeout).mkString(";")
          HttpResponse(StatusCodes.OK, entity = HttpEntity(ids))
        }

      }
    } ~
    path("getComeOutLogs") {
      get {
        complete {
          val ids = Await.result(loggerService.getLogs(Out), timeout).mkString(";")
          HttpResponse(StatusCodes.OK, entity = HttpEntity(ids))
        }
      }
    }
}
