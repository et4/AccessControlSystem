package client.services

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, Uri}
import akka.http.scaladsl.model.Uri.Query

import scala.concurrent.Future

class LogService extends Service {
  def getAnomalies(cardId: Int, access: Boolean): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri("http://localhost:8182/getAnomalies").withQuery(Query(
        "cardId" -> cardId.toString,
        "access" -> access.toString))))

  def getTimeInside(groupId: Int, access: Boolean): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri("http://localhost:8182/getTimeInside").withQuery(Query(
        "groupId" -> groupId.toString,
        "access" -> access.toString))))

  def getLogs(access: Boolean): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri("http://localhost:8182/getLogs").withQuery(Query(
        "access" -> access.toString))))
}
