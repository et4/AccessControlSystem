package server.services

import server.tables.{Group, GroupAccess, GroupAccessTable, GroupTable}
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait GroupService {
  def addEmptyGroup(access: Boolean): Future[Int]
  def addGroup(cardsId: Seq[Int], access: Boolean): Future[Seq[Int]]
  def setGroupAccess(groupId: Int, access: Boolean): Future[Int]
  def setExceptionalAccess(cardId: Int, groupId: Int, access: String): Future[Int]
  def setGroupToCard(cardId: Int, groupId: Int): Future[Int]
  def setGroupToCards(cardIds: Seq[Int], groupId: Int): Future[Seq[Int]]
  def kickFromGroup(cardId: Int, groupId: Int): Future[Int]
}

class GroupServiceImpl(implicit db: Database) extends GroupService {
  val groups = TableQuery[GroupTable]
  val groupAccess = TableQuery[GroupAccessTable]

  def addEmptyGroup(access: Boolean): Future[Int] = {
    db.run((groups returning groups.map(_.id)) += Group(None, access))
  }

  def addGroup(cardsId: Seq[Int], access: Boolean): Future[Seq[Int]] = {
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
      groupAccess
        .filter(_.groupId === groupId.bind)
        .filter(_.cardId === cardId.bind)
        .map(_.access)
        .update(access)
    )
  }

  def setGroupToCard(cardId: Int, groupId: Int): Future[Int] = {
    db.run(groupAccess.insertOrUpdate(GroupAccess(cardId, groupId, "DEFAULT")))
  }

  def setGroupToCards(cardIds: Seq[Int], groupId: Int): Future[Seq[Int]] = {
    Future.traverse(cardIds)(setGroupToCard(_, groupId))
//    db.run(
//      DBIO.sequence(
//        cardIds.map {
//          cardId => groupAccess.insertOrUpdate(GroupAccess(cardId, groupId, "DEFAULT"))
//        }
//      )
//    )
  }

  def kickFromGroup(cardId: Int, groupId: Int): Future[Int] = {
    db.run(
      groupAccess
        .filter(_.groupId === groupId.bind)
        .filter(_.cardId === cardId.bind)
        .delete
    )
  }
}