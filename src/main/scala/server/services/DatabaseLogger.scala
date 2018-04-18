package server.services

import java.sql.Time

import server.tables.{Log, LogTable}
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

class DatabaseLogger(db: Database) {
  val logs = TableQuery[LogTable]

  def log(cardId: Int, time: Time, eventType: Boolean, success: Boolean): Unit = {
    db.run(logs += Log(cardId, time, eventType, success))
  }
}
