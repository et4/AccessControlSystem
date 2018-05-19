package server


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import server.controllers.{PermissionsController, TurnstileController}
import server.services.{CardService, CardServiceImpl, GroupService, GroupServiceImpl}

import scala.concurrent.Future

class RestServer(implicit val system: ActorSystem,
                 implicit val materializer: ActorMaterializer) {
  def startServer(route: Route, address: String, port: Int): Future[Http.ServerBinding] = {
    Http().bindAndHandle(route, address, port)
  }
}

object RestServer {
  import slick.jdbc.H2Profile
  import slick.jdbc.JdbcBackend.Database

  def main(args: Array[String]) {
    implicit val actorSystem: ActorSystem = ActorSystem("rest-server")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    implicit val db: Database = Database.forConfig("db")

    val cardService: CardService = new CardServiceImpl(H2Profile)
    val groupService: GroupService = new GroupServiceImpl(H2Profile)

    val permissionsController = new PermissionsController(cardService, groupService)
    val turnstileController = new TurnstileController(cardService)

    val server = new RestServer()
    server.startServer(permissionsController.routes ~ turnstileController.routes, "localhost", 8182)
  }
}