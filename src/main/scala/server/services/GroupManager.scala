package server.services

import server.tables.{Group, GroupTable}
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GroupManager(cardManager: CardManager)(implicit db: Database) {
  val groups = TableQuery[GroupTable]

  def addEmptyGroup(access: Boolean): Future[Int] = {
    db.run((groups returning groups.map(_.id)) += Group(None, access))
  }

  def addGroup(cardsId: Seq[Int], access: Boolean): Unit = {
    addEmptyGroup(access).map(groupId => cardManager.setGroupToCards(cardsId, groupId))
  }

  def setGroupAccess(groupId: Int, access: Boolean): Unit = {
    db.run(groups
      .filter(_.id === groupId.bind)
      .map(_.hasAccess)
      .update(access)
    )
  }
}
