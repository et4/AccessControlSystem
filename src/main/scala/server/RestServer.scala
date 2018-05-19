package server


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import server.controllers.{PermissionsController, TurnstileController}
import server.services.{CardService, CardServiceImpl, GroupService, GroupServiceImpl}
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
    val cardService: CardService = new CardServiceImpl()
    val groupService: GroupService = new GroupServiceImpl()

    val permissionsController = new PermissionsController(cardService, groupService)
    val turnstileController = new TurnstileController(cardService)

    val server = new RestServer()
    server.startServer(permissionsController.routes ~ turnstileController.routes, "localhost", 8182)
  }
}