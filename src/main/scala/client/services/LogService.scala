package client.services

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, Uri}
import akka.http.scaladsl.model.Uri.Query

import scala.concurrent.Future

class LogService extends Service {
  def getAnomalies(fromDateTime: Long, toDateTime: Long, times: Int): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/getAnomalies").withQuery(Query(
        "fromDateTime" -> fromDateTime.toString,
        "toDateTime" -> toDateTime.toString,
        "times" -> times.toString))))

  def getTimeInside(cardId: Int, fromDateTime: Long, toDateTime: Long): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/getTimeInside").withQuery(Query(
        "cardId" -> cardId.toString,
        "fromDateTime" -> fromDateTime.toString,
        "toDateTime" -> toDateTime.toString))))

  def getComeInLogs(access: Boolean): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/getComeInLogs")))

  def getComeOutLogs(access: Boolean): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/getComeOutLogs")))
}
