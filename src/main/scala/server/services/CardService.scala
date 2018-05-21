package server.services

import server.Card
import server.models._
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait CardService {
  def hasAccess(cardId: Int): Future[Boolean]

  def setIndividualAccess(cardId: Int, access: Boolean): Future[Option[Card]]

  def getAllCards: Future[Seq[Card]]
}

class CardServiceImpl(val profile: JdbcProfile)(implicit db: JdbcBackend.Database)
  extends CardService with CardModel with GroupAccessModel {

  import profile.api._

  /**
    * @return sequence of tuples - (Individual access, Exceptional access, Group Access)
    */
  private def cardAccessData(cardId: Int): Future[Seq[(Boolean, Option[String], Option[Boolean])]] = {
    db.run(
      cards
        .filter(_.id === cardId.bind)
        .joinLeft(TableQuery[GroupAccessTable])
        .on(_.id === _.cardId)
        .map { case (card, gAccess) => (card.hasAccess, gAccess.map(_.access), gAccess.map(_.groupId)) }
        .joinLeft(TableQuery[GroupTable])
        .on(_._3 === _.id)
        .map(x => (x._1._1, x._1._2, x._2.map(_.hasAccess)))
        .map { case (cardAccess, exception, groupAccess) => (cardAccess, exception, groupAccess) }
        .result
    )
  }

  def hasAccess(cardId: Int): Future[Boolean] = {
    cardAccessData(cardId)
      .map(_.unzip3)
      .map(x => (x._1, x._2 zip x._3))
      .map(z =>
        (z._1.head,
         z._2.map {
           case (Some("GRANTED"), _) => Some(true)
           case (Some("FORBIDDEN"), _) => Some(false)
           case (Some("DEFAULT"), Some(hasAccess)) => Some(hasAccess)
           case (None, None) => None
         }.reduce[Option[Boolean]] {
           case (Some(a), Some(b)) => Some(a || b)
           case (Some(a), None) => Some(a)
           case (None, Some(b)) => Some(b)
           case (None, None) => None
         }
        )
      )
      .map {
        case (_, Some(access)) => access
        case (access, None) => access
    }
  }

  def setIndividualAccess(cardId: Int, access: Boolean): Future[Option[Card]] = {
    val row = cards.filter(_.id === cardId)
    db.run(row.map(_.hasAccess).update(access).andThen(row.result.headOption))
  }

  override def getAllCards: Future[Seq[Card]] = {
    db.run(cards.result)
  }
}
