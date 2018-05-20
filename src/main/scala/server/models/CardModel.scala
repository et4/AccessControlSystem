package server.models

import server.Card

trait CardModel extends DatabaseModel {
  import profile.api._

  final class CardTable(tag: Tag) extends Table[Card](tag, "card") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def hasAccess = column[Boolean]("hasaccess")

    def * = (id.?, hasAccess) <> (Card.tupled, Card.unapply)
  }

  val cards = TableQuery[CardTable]
}

