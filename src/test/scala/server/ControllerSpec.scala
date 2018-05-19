package server

import java.time.Instant

import org.scalatest.{FlatSpecLike, Matchers, WordSpec}
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes, Uri}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.pattern.FutureRef
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpecLike, Matchers}
import server.controllers.TurnstileController
import server.services.CardServiceImpl
import slick.jdbc
import slick.jdbc.H2Profile

import scala.concurrent.Future

class ControllerSpec extends FlatSpecLike with ScalatestRouteTest with Matchers with MockFactory {

  trait Setup {
    implicit val db: jdbc.H2Profile.backend.Database = mock[H2Profile.backend.Database]
    val cardManager: CardServiceImpl = mock[CardServiceImpl]
    lazy val controller = new TurnstileController(cardManager)
    lazy val routes: Route = Route.seal(controller.routes)
  }

  "Get /action" should "check existing permissions" in new Setup {
    val id = 1
    (cardManager.hasAccess _).expects(id).returning(Future.successful(true))
    HttpRequest(HttpMethods.GET,
      Uri("http://localhost:8182/action").withQuery(Query(
        "cardId" -> id.toString,
        "date" -> Instant.now().toString,
        "event" -> "exit"))) ~> Route.seal(routes) ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  "Get /action" should "check not existing permissions" in new Setup {
    val id = 2
    (cardManager.hasAccess _).expects(id).returning(Future.successful(false))
    HttpRequest(HttpMethods.GET,
      Uri("http://localhost:8182/action").withQuery(Query(
        "cardId" -> id.toString,
        "date" -> Instant.now().toString,
        "event" -> "exit"))) ~> Route.seal(routes) ~> check {
      status shouldEqual StatusCodes.Forbidden
    }
  }
}