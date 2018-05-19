package server.models

trait CardModel extends DatabaseModel {
  import profile.api._

  final case class Card(
                         id : Option[Int],
                         hasAccess : Boolean,
                       )

  final class CardTable(tag: Tag) extends Table[Card](tag, "CARD") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def hasAccess = column[Boolean]("HASACCESS")

    def * = (id.?, hasAccess) <> (Card.tupled, Card.unapply)
  }

  val cards = TableQuery[CardTable]
}

