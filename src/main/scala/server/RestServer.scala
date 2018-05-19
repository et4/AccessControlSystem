package server


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import client.Turnstile
import server.controllers.{PermissionsController, TurnstileController}
import server.services.{CardServiceImpl, GroupServiceImpl}
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

class RestServer(implicit val system: ActorSystem,
                 implicit val materializer: ActorMaterializer) {
  def startServer(route: Route, address: String, port: Int): Future[Http.ServerBinding] = {
    Http().bindAndHandle(route, address, port)
  }
}

object RestServer {

  def main(args: Array[String]) {
    implicit val actorSystem: ActorSystem = ActorSystem("rest-server")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    implicit val db: H2Profile.backend.Database = Database.forConfig("db")
    val cardManager: CardServiceImpl = new CardServiceImpl()
    val permissionsController = new PermissionsController(cardManager)
    val turnstileController = new TurnstileController(cardManager)
    val server = new RestServer()
    server.startServer(permissionsController.routes ~ turnstileController.routes, "localhost", 8182)
  }
}