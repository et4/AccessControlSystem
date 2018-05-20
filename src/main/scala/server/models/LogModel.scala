package server.models

import java.sql.Timestamp

import server.Log

trait LogModel extends DatabaseModel with CardModel {

  import profile.api._

  final class LogTable(tag: Tag) extends Table[Log](tag, "log") {
    def cardId = column[Int]("cardid")

    def dateTime = column[Timestamp]("datetime")

    def eventType = column[String]("eventtype")

    def success = column[Boolean]("success")

    def * = (cardId, dateTime, eventType, success) <> (Log.tupled, Log.unapply)

    def card = foreignKey("fk_Log_cardId", cardId, TableQuery[CardTable])(_.id)
  }

  val logs = TableQuery[LogTable]
}