package server.services

import server.tables.{UserGroup, UserGroupTable}
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

class GroupManager(db: Database, cardManager: CardManager) {
  val groups = TableQuery[UserGroupTable]

  def addEmptyGroup(access: Boolean): Future[Int] = {

    val actionWithGroupId = (groups returning groups.map(_.id)) += UserGroup(None, access)

    db.run(actionWithGroupId)
  }

  def addGroup(usersId: Seq[Int], access: Boolean): Unit = {
    addEmptyGroup(access).onComplete(_.map(groupId => {
      cardManager.setGroupToCards(usersId, groupId)
    }).orElse(throw new Exception("RIP")))
  }
}
