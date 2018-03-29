package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.Future

object Server extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val serverSource = Http().bind(interface = "localhost", port = 8182)

  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(GET, Uri.Path("/open"), headers, _, _) =>
      val header = headers.find(header => header.name() == "id").get
      if (header.value().toInt < 500000)
        HttpResponse(StatusCodes.OK)
      else HttpResponse(StatusCodes.Forbidden)
    //      HttpResponse(entity = HttpEntity(
    //        ContentTypes.`application/json`,
    //        "{"id": 1, "param": 2}"))

    case HttpRequest(GET, Uri.Path("/addCardPermission"), headers, entity, _) => ???

    case HttpRequest(GET, Uri.Path("/deleteCardPermission"), headers, entity, _) => ???

    case HttpRequest(GET, Uri.Path("/addGroupPermission"), headers, entity, _) => ???

    case HttpRequest(GET, Uri.Path("/addGroupExclusion"), headers, entity, _) => ???
  }

  val bindingFuture: Future[Http.ServerBinding] =
    serverSource.to(Sink.foreach { connection =>
      connection handleWithSyncHandler requestHandler
    }).run()

  def changeKeyPermissions(keyId: Int): Unit = {

  }
}