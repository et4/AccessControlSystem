package server

import java.sql.Timestamp

import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes, Uri}
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpecLike, Matchers}
import server.controllers.TurnstileController
import server.services.{CardService, LoggerService}
import slick.jdbc
import slick.jdbc.H2Profile

import scala.concurrent.Future

class ControllerSpec extends FlatSpecLike with ScalatestRouteTest with Matchers with MockFactory {

  trait Setup {
    implicit val db: jdbc.H2Profile.backend.Database = mock[H2Profile.backend.Database]
    val cardManager: CardService = mock[CardService]
    val loggerService: LoggerService = mock[LoggerService]
    lazy val controller = new TurnstileController(cardManager, loggerService)
    lazy val routes: Route = Route.seal(controller.routes)
  }

  "Get /action" should "check existing permissions" in new Setup {
    val id = 1
    (cardManager.hasAccess _).expects(id).returning(Future.successful(Some(true)))
    (loggerService.log _).expects(id, new Timestamp(11), "IN", true)
      .returning(Future.successful(Log(id, new Timestamp(11), "IN", true)))
    HttpRequest(HttpMethods.GET,
      Uri("http://localhost:8182/action").withQuery(Query(
        "cardId" -> id.toString,
        "date" -> "11",
        "event" -> "IN"))) ~> Route.seal(routes) ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  "Get /action" should "check not existing permissions" in new Setup {
    val id = 2
    (cardManager.hasAccess _).expects(id).returning(Future.successful(Some(false)))
    (loggerService.log _).expects(id, new Timestamp(11), "IN", false)
      .returning(Future.successful(Log(id, new Timestamp(11), "IN", false)))
    HttpRequest(HttpMethods.GET,
      Uri("http://localhost:8182/action").withQuery(Query(
        "cardId" -> id.toString,
        "date" -> "11",
        "event" -> "IN"))) ~> Route.seal(routes) ~> check {
      status shouldEqual StatusCodes.Forbidden
    }
  }
}