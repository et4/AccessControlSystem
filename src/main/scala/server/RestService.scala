package server

import java.util.concurrent.ConcurrentLinkedDeque

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import client.Card
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import slick.jdbc.H2Profile.api._
import server.services.{CardManager, GroupManager}
import slick.jdbc.H2Profile
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.Duration


trait RestService {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  //  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val db: H2Profile.backend.Database = Database.forConfig("db")

  val cardManager: CardManager = new CardManager()
  val groupManager: GroupManager = new GroupManager(cardManager)
  val serverSource = Http().bind(interface = "localhost", port = 8182)

  val route = {
    path("action") {
      get {
        parameters('cardId.as[String], 'date.as[String], 'event.as[String]) { (cardId, date, event) =>
          complete {
            if (Await.result(cardManager.hasAccess(cardId.toInt), Duration.Inf))
              HttpResponse(StatusCodes.OK)
            else
              HttpResponse(StatusCodes.Forbidden)
          }
        }
      }
    }
  } ~
    path("addCardPermission") {
      get {
        parameters('cardId.as[String]) { (cardId) =>
          complete {
            cardManager.setIndividualAccess(cardId.toInt, true)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("deleteCardPermission") {
      get {
        parameters('cardId.as[String]) { (cardId) =>
          complete {
            cardManager.setIndividualAccess(cardId.toInt, false)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("addGroupPermission") {
      get {
        parameters('groupId.as[String]) { (groupId) =>
          complete {
            groupManager.setGroupAccess(groupId.toInt, true)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("addGroupExclusion") {
      get {
        parameters('groupId.as[String]) { (groupId) =>
          complete {
            groupManager.setGroupAccess(groupId.toInt, false)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("forTest") {
      get {
        complete {
          HttpResponse(StatusCodes.Accepted)
        }
      }
    }
}