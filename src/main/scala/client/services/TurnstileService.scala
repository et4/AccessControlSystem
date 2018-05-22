package client.services

import java.time.Instant

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import client.domain.{Card, EntranceEvent, ExitEvent, TurnstileEvent}

import scala.concurrent.{Await, Future}
import scala.util.Success

class TurnstileService extends Service {
  def requestAccess(cardId: Int, date: Instant = Instant.now(), eventType: TurnstileEvent): Unit = {

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(HttpMethods.GET,
        Uri(uri + "/action").withQuery(Query(
          "cardId" -> cardId.toString,
          "date" -> date.toString,
          "event" -> eventTypeToString(eventType)))))

    responseFuture.onComplete {
      case Success(res) =>
        if (res.status == StatusCodes.OK)
          println("Access is allowed")
        else println("Access is not allowed")
      case _ =>
        sys.error("something wrong")
    }
  }

  def requestAllCardsIds(): String = {

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(HttpMethods.GET,
        Uri(uri + "/getAllCards")))

    var cardsIds = "1"
    responseFuture.onComplete {
      case Success(res) =>
        cardsIds = Await.result(Unmarshal(res.entity).to[String], timeout)
      case _ =>
        sys.error("something wrong")
    }
    cardsIds
  }

  def requestAccess(): Boolean = {

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(HttpMethods.GET,
        Uri(uri + "/hasAccess")))

    var access = false
    responseFuture.onComplete {
      case Success(res) =>
        access = Await.result(Unmarshal(res.entity).to[String], timeout).toBoolean
      case _ =>
        sys.error("something wrong")
    }
    access
  }

  def eventTypeToString(eventType: TurnstileEvent): String = {
    eventType match {
      case event: EntranceEvent => "IN"
      case event: ExitEvent => "OUT"
      case _ => ""
    }
  }
}
