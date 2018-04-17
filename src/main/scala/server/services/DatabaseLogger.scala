package server.services

import slick.jdbc.H2Profile.api._
import java.sql.Time
import java.util.concurrent.TimeUnit

import server.tables.{Log, LogTable}
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object DatabaseLogger {
  def log(cardId: Int, time: Time, eventType: Boolean, success: Boolean): Unit = {
    var logs = TableQuery[LogTable]
    val action = logs += Log(cardId, time, eventType, success)
    Await.result(Database.forConfig("db").run(action), Duration(2, TimeUnit.SECONDS))
  }
}
