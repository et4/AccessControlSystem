package server.services

import java.sql.Timestamp

import server.Log
import server.models.LogModel
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

sealed trait QueryFilter
case object In extends QueryFilter
case object Out extends QueryFilter
case object All extends QueryFilter

trait LoggerService {
  def log(cardId: Int, time: Timestamp, eventType: String, success: Boolean): Future[Log]

  def getLogs(queryFilter: QueryFilter): Future[Seq[Log]]

  def getLogsByCard(cardId: Int): Future[Seq[Log]]

  def getAnomalies(fromDateTime: Timestamp, toDateTime: Timestamp, times: Int): Future[Iterable[Int]]

  def getTimeInside(cardId: Int, fromDateTime: Timestamp, toDateTime: Timestamp): Future[Long]
}

class DatabaseLoggerServiceImpl(val profile: JdbcProfile)(implicit db: JdbcBackend.Database)
  extends LoggerService with LogModel {

  import profile.api._

  def log(cardId: Int, date: Timestamp, eventType: String, success: Boolean): Future[Log] = {
    db.run(logs += Log(cardId, date, eventType, success)).map[Log](_ => Log(cardId, date, eventType, success))
  }

  def getLogs(queryFilter: QueryFilter): Future[Seq[Log]] = {
    queryFilter match {
      case All => db.run(logs.result)
      case In => db.run(logs.filter(_.eventType === "IN".bind).result)
      case Out => db.run(logs.filter(_.eventType === "OUT".bind).result)
    }
  }

  def getLogsByCard(cardId: Int): Future[Seq[Log]] = {
    db.run(logs.filter(_.cardId === cardId.bind).result)
  }

  def getAnomalies(fromDateTime: Timestamp, toDateTime: Timestamp, times: Int): Future[Iterable[Int]] = {
    db.run(
      logs
        .filter(_.success === false)
        .filter(date => fromDateTime.bind <= date.dateTime && date.dateTime <= toDateTime.bind)
        .groupBy(_.cardId)
        .map { case (cardId, events) => (cardId, events.length) }
        .filter(_._2 >= times)
        .map(_._1)
        .result
    )
  }

  def getTimeInside(cardId: Int, fromDateTime: Timestamp, toDateTime: Timestamp): Future[Long] = {
    def addClosingBounds(cardLogs: Seq[Log]): Long = {
      var logs = cardLogs

      if (cardLogs.head.eventType == "OUT") logs = Log(cardId, fromDateTime, "IN", true) +: logs
      if (cardLogs.last.eventType == "IN") logs = logs :+ Log(cardId, toDateTime, "OUT", true)

      logs.foldLeft(0L)(
        (b, log) =>
          if (log.eventType == "IN")
            b - log.dateTime.getTime
          else
            b + log.dateTime.getTime
      )
    }

    db.run(
      logs
        .filter(_.cardId === cardId.bind)
        .filter(_.success === true)
        .filter(log => fromDateTime.bind <= log.dateTime && log.dateTime <= toDateTime.bind)
        .sortBy(_.dateTime)
        .result
    ).map[Long](cardLogs => if (cardLogs.isEmpty) 0L else addClosingBounds(cardLogs))
  }
}