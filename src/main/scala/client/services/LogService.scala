package client.services

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, Uri}
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.{Await, Future}
import scala.util.Success

class LogService extends Service {
  def getAnomalies(fromDateTime: Long, toDateTime: Long, times: Int): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/getAnomalies").withQuery(Query(
        "fromDateTime" -> fromDateTime.toString,
        "toDateTime" -> toDateTime.toString,
        "times" -> times.toString))))

  def getTimeInside(cardId: Int, fromDateTime: Long, toDateTime: Long): Seq[String] = {
    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(HttpMethods.GET,
        Uri(uri + "/getTimeInside").withQuery(Query(
          "cardId" -> cardId.toString,
          "fromDateTime" -> fromDateTime.toString,
          "toDateTime" -> toDateTime.toString))))

    var ids = Seq.empty[String]
    responseFuture.onComplete {
      case Success(res) =>
        ids = Await.result(Unmarshal(res.entity).to[String], timeout).split(";")
      case _ =>
        sys.error("something wrong")
    }
    ids
  }

  def getComeInLogs: Seq[String] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/getComeInLogs")))

    var ids = Seq.empty[String]
    responseFuture.onComplete {
      case Success(res) =>
        val t = Await.result(Unmarshal(res.entity).to[String], timeout).split(";")
        ids = t
      case _ =>
        sys.error("something wrong")
    }
    ids
  }

  def getComeOutLogs: Seq[String] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/getComeOutLogs")))

    var ids = Seq.empty[String]
    responseFuture.onComplete {
      case Success(res) =>
        ids = Await.result(Unmarshal(res.entity).to[String], timeout).split(";")
      case _ =>
        sys.error("something wrong")
    }
    ids
  }
}
