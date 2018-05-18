package server.services

import java.sql.Time

import server.tables.{Log, LogTable}
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

sealed trait QueryFilter
case object In extends QueryFilter
case object Out extends QueryFilter
case object All extends QueryFilter

class DatabaseLogger(implicit db: Database) {
  val logs = TableQuery[LogTable]

  def log(cardId: Int, time: Time, eventType: String, success: Boolean): Future[Int] = {
    db.run(logs += Log(cardId, time, eventType, success))
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

  def getAnomalies(fromDateTime: Time, toDateTime: Time, times: Int): Future[Iterable[Int]] = {
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