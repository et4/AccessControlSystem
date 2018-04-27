package server
import slick.jdbc.H2Profile.backend.Database
import server.services.{CardManager, DatabaseLogger, GroupManager}
import scala.concurrent.ExecutionContext.Implicits.global

object Kek extends App {
  implicit val db = Database.forConfig("db")

  private val manager = new CardManager()
  private val logger = new DatabaseLogger()
  private val manager1 = new GroupManager(manager)

  manager.hasAccess(1).andThen({case x => Thread.sleep(4000); println("1: " + x.get)})
  manager.hasAccess(2).andThen({case x => Thread.sleep(4000); println("2: " + x.get)})
  manager.hasAccess(3).andThen({case x => Thread.sleep(4000); println("3: " + x.get)})
  manager.hasAccess(4).andThen({case x => Thread.sleep(4000); println("4: " + x.get)})
  manager.hasAccess(5).andThen({case x => Thread.sleep(4000); println("5: " + x.get)})
  manager.hasAccess(6).andThen({case x => Thread.sleep(4000); println("6: " + x.get)})
  manager.hasAccess(7).andThen({case x => Thread.sleep(4000); println("7: " + x.get)})
  manager.hasAccess(8).andThen({case x => Thread.sleep(4000); println("8: " + x.get)})
  Thread.sleep(10000)

}
