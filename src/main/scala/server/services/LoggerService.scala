package server.services

import java.sql.Timestamp

import server.Log
import server.models.LogModel
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.Future

sealed trait QueryFilter
case object In extends QueryFilter
case object Out extends QueryFilter
case object All extends QueryFilter

trait LoggerService {
  def log(cardId: Int, time: Timestamp, eventType: String, success: Boolean): Future[Int]

  def getLogs(queryFilter: QueryFilter): Future[Seq[Log]]

  def getLogsByCard(cardId: Int): Future[Seq[Log]]

  def getAnomalies(fromDateTime: Timestamp, toDateTime: Timestamp, times: Int): Future[Iterable[Int]]
}

class DatabaseLoggerServiceImpl(val profile: JdbcProfile)(implicit db: JdbcBackend.Database)
  extends LoggerService with LogModel {

  import profile.api._

  def log(cardId: Int, date: Timestamp, eventType: String, success: Boolean): Future[Int] = {
    db.run(logs += Log(cardId, date, eventType, success))
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
}