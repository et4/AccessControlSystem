package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer


import scala.io.StdIn

object Server extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

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
              ???
            }
          }
        }
      } ~
      path("deleteCardPermission") {
        get {
          parameters('cardId.as[String]) { (cardId) =>
            complete {
              ???
            }
          }
        }
      } ~
      path("addGroupPermission") {
        get {
          parameters('groupId.as[String]) { (groupId) =>
            complete {
              ???
            }
          }
        }
      } ~
      path("addGroupExclusion") {
        get {
          parameters('id.as[String], 'date.as[String], 'event.as[String]) { (id, date, event) =>
            complete {
              ???
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