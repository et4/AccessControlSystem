package server.tables

import java.sql.Time

import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

final case class Log(
                      cardId : Int,
                      dateTime : Time,
                      eventType : Boolean,
                      success : Boolean
                     )

final class LogTable(tag: Tag) extends Table[Log](tag, "LOG") {
  def cardId = column[Int]("CARDID")

  def dateTime = column[Time]("DATETIME")

  def eventType = column[Boolean]("EVENTTYPE")

  def success = column[Boolean]("SUCCESS")

  def * = (cardId, dateTime, eventType, success) <> (Log.tupled, Log.unapply)

  def card = foreignKey("fk_Log_cardId", cardId, TableQuery[CardTable])(_.id)
}