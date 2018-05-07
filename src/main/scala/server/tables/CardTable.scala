package server.tables

import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

final case class Card(
                     id : Option[Int],
                     hasAccess : Boolean,
                     )

final class CardTable(tag: Tag) extends Table[Card](tag, "CARD") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def hasAccess = column[Boolean]("HASACCESS")

  def * = (id.?, hasAccess) <> (Card.tupled, Card.unapply)
}