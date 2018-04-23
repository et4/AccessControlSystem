package client

import java.time.Instant

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class Turnstile {
  def requestAccess(card: Card, date: Instant = Instant.now(), eventType: TurnstileEvent): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    import scala.concurrent.duration._
    val timeout = 300.millis

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(HttpMethods.GET,
        Uri("http://localhost:8182/open").withQuery(Query(
          "id" -> card.cardId.toString,
          "date" -> date.toString,
          "event" -> getEventType(eventType)))))

    responseFuture.onComplete {
      case Success(res) =>
        if (res.status == StatusCodes.OK)
          println("Доступ разрешен")
        else println("Доступ не разрешен")
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
