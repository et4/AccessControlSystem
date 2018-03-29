package client

import java.util.{Calendar, Date}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class Turnstile {
  def getAccsess(card: Card, date: Date = Calendar.getInstance.getTime, eventType: TurnstileEvent): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    import scala.concurrent.duration._
    val timeout = 300.millis

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(HttpMethods.GET, Uri("http://localhost:8182/open"))
        .withHeaders(
          RawHeader("id", card.cardId.toString),
          RawHeader("date", date.toString),
          RawHeader("event", getEventType(eventType))))

    responseFuture.onComplete {
      case Success(res) =>
        if (res.status == StatusCodes.OK)
          println("Доступ разрешен")
        else println("Доступ не разрешен")
      //        println(res.entity.toStrict(timeout).map {
      //          _.data
      //        }.map(_.utf8String))
      //        println(Unmarshal(res.entity).to[String].value.map(_.get))
      case _ =>
        sys.error("something wrong")
    }
  }

  def getEventType(eventType: TurnstileEvent): String = {
    eventType match {
      case event: EntranceEvent => event.eventName
      case event: ExitEvent => event.eventName
      case _ => ""
    }
  }
}
