package server.tables

import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

final case class Card(
                     id : Option[Int],
                     hasAccess : Boolean,
                     priorityAccess : Boolean,
                     groupId : Option[Int]
                     )

final class CardTable(tag: Tag) extends Table[Card](tag, "CARD") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def hasAccess = column[Boolean]("HASACCESS")

  def priorityAccess = column[Boolean]("PRIORITYACCESS")

  def groupId = column[Option[Int]]("GROUPID")

  def * = (id.?, hasAccess, priorityAccess, groupId) <> (Card.tupled, Card.unapply)
}