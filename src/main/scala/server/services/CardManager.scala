package server.services

import server.tables.{CardTable, UserGroupTable}
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class CardManager(implicit db: Database) {
  val cards = TableQuery[CardTable]

  //TODO: PriorityAccess

  def hasAccess(cardId: Int): Future[Boolean] = {
    db.run(cards
      .filter(_.id === cardId.bind)
      .joinLeft(TableQuery[UserGroupTable])
      .on(_.groupId === _.id)
      .map(t => (t._1.hasAccess, t._2.map(_.hasAccess)))
      .result
      .headOption)
      .map {
        case Some((_, Some(true))) => true
        case Some((true, _)) => true
        case _ => false
      }
  }

  def setAccess(cardId: Int, access: Boolean): Unit = {
    val updateQuery = cards.filter(_.id === cardId).map(_.hasAccess).update(access)
    db.run(updateQuery)
  }

  def setHighPriorityAccess(cardId: Int, access: Boolean): Unit = ???

  def setGroupToCard(cardId: Int, groupId: Int): Unit = {
    db.run(cards
      .filter(_.id === cardId)
      .map(_.groupId)
      .update(Some(groupId)))
  }

  def setGroupToCards(cardIds: Seq[Int], groupId: Int): Unit = {
    db.run(cards
      .filter(_.id inSetBind cardIds)
      .map(_.groupId)
      .update(Some(groupId)))
  }

  def kickFromGroup(cardId: Int, groupId: Int): Unit = {
    db.run(cards
      .filter(_.id === cardId)
      .map(_.groupId)
      .update(None))
  }
}
