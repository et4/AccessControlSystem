package server
import slick.jdbc.H2Profile.backend.Database
import server.services.{CardManager, DatabaseLogger, GroupManager}

object Test extends App {
  implicit val db = Database.forConfig("db")

  private val manager = new CardManager()
  private val logger = new DatabaseLogger()
  private val manager1 = new GroupManager(manager)
}
