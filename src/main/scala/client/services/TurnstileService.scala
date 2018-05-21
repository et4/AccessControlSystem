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
        Uri("http://localhost:8182/action").withQuery(Query(
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
        Uri("http://localhost:8182/getAllCards")))

    var cardsId = "1"
    responseFuture.onComplete {
      case Success(res) =>
        cardsId = Await.result(Unmarshal(res.entity).to[String], timeout)
      case _ =>
        sys.error("something wrong")
    }
    cardsId
  }

  def eventTypeToString(eventType: TurnstileEvent): String = {
    eventType match {
      case event: EntranceEvent => event.eventName
      case event: ExitEvent => event.eventName
      case _ => ""
    }
  }
}
