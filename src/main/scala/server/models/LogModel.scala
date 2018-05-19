package server.models

import java.sql.Time

trait LogModel extends DatabaseModel with CardModel {

  import profile.api._

  final case class Log(
                        cardId: Int,
                        dateTime: Time,
                        eventType: String,
                        success: Boolean
                      )

  final class LogTable(tag: Tag) extends Table[Log](tag, "LOG") {
    def cardId = column[Int]("CARDID")

    def dateTime = column[Time]("DATETIME")

    def eventType = column[String]("EVENTTYPE")

    def success = column[Boolean]("SUCCESS")

    def * = (cardId, dateTime, eventType, success) <> (Log.tupled, Log.unapply)

    def card = foreignKey("fk_Log_cardId", cardId, TableQuery[CardTable])(_.id)
  }

  val logs = TableQuery[LogTable]
}