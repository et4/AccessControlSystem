package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Sink
import server.services.{CardManager, GroupManager}
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.io.StdIn

object Server extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val db: H2Profile.backend.Database = Database.forConfig("db")

  val cardManager: CardManager = new CardManager()
  val groupManager: GroupManager = new GroupManager()
  val serverSource = Http().bind(interface = "localhost", port = 8182)

  val route = {
    path("open") {
      get {
        parameters('id.as[String], 'date.as[String], 'event.as[String]) { (id, date, event) =>
          complete {
            if (id.toInt < 500000)
              HttpResponse(StatusCodes.OK)
            else HttpResponse(StatusCodes.Forbidden)
          }
        }
      }
    } ~
      path("addCardPermission") {
        get {
          parameters('cardId.as[String]) { (cardId) =>
            complete {
              cardManager.setAccess(cardId.toInt, true)
              HttpResponse(StatusCodes.OK)
            }
          }
        }
      } ~
      path("deleteCardPermission") {
        get {
          parameters('cardId.as[String]) { (cardId) =>
            complete {
              cardManager.setAccess(cardId.toInt, false)
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
            }
          }
        }
      }
  }


  val bindingFuture = Http().bindAndHandle(route, "localhost", 8182)

  println("Server online at http://localhost:8080/")
  println("Press RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}