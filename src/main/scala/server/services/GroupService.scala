package server.services

import server.models._
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait GroupService {
  def addEmptyGroup       (access: Boolean): Future[Int]
  
  def setGroupAccess      (groupId: Int, access: Boolean): Future[Int]

  def setExceptionalAccess(cardId: Int, groupId: Int, access: String): Future[Int]

  def setGroupToCard      (cardId: Int, groupId: Int): Future[Int]

  def kickFromGroup       (cardId: Int, groupId: Int): Future[Int]

  def createGroupForCards (cardsId: Seq[Int], access: Boolean): Future[Seq[Int]]

  def setGroupToCards     (cardsId: Seq[Int], groupId: Int): Future[Seq[Int]]
}

class GroupServiceImpl(val profile: JdbcProfile)(implicit db: JdbcBackend.Database)
  extends GroupService with GroupModel with GroupAccessModel {

  import profile.api._

  def addEmptyGroup(access: Boolean): Future[Int] = {
    db.run((groups returning groups.map(_.id)) += Group(None, access))
  }

  def createGroupForCards(cardsId: Seq[Int], access: Boolean): Future[Seq[Int]] = {
    addEmptyGroup(access).map(groupId => setGroupToCards(cardsId, groupId)).flatten
  }

  def setGroupAccess(groupId: Int, access: Boolean): Future[Int] = {
    db.run(
      groups
      .filter(_.id === groupId.bind)
      .map(_.hasAccess)
      .update(access)
    )
  }

  def setExceptionalAccess(cardId: Int, groupId: Int, access: String): Future[Int] = {
    db.run(
      groupsAccess
        .filter(_.groupId === groupId.bind)
        .filter(_.cardId === cardId.bind)
        .map(_.access)
        .update(access)
    )
  }

  def setGroupToCard(cardId: Int, groupId: Int): Future[Int] = {
    db.run(groupsAccess.insertOrUpdate(GroupAccess(cardId, groupId, "DEFAULT")))
  }

  def setGroupToCards(cardsId: Seq[Int], groupId: Int): Future[Seq[Int]] = {
    Future.traverse(cardsId)(setGroupToCard(_, groupId))
  }

  def kickFromGroup(cardId: Int, groupId: Int): Future[Int] = {
    db.run(
      groupsAccess
        .filter(_.groupId === groupId.bind)
        .filter(_.cardId === cardId.bind)
        .delete
    )
  }
}
