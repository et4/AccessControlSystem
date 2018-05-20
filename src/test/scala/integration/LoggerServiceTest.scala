package integration

import java.sql.Timestamp
import java.time.Instant

import server.Log
import server.services.{All, DatabaseLoggerServiceImpl, In, Out}
import slick.jdbc.PostgresProfile
import scala.concurrent.duration._

import scala.concurrent.{Await, Future}

import scala.concurrent.ExecutionContext.Implicits.global

class LoggerServiceTest extends FutureTest with DatabaseTest {
  val loggerService = new DatabaseLoggerServiceImpl(PostgresProfile)(database)
  val timestamp = Timestamp.from(Instant.now())

  override protected def beforeAll(): Unit = {

    val seq: Seq[Future[Int]] = Seq(
      loggerService.log(1, timestamp, "IN", true),
      loggerService.log(2, timestamp, "IN", false),
      loggerService.log(2, timestamp, "OUT", true),
      loggerService.log(3, timestamp, "OUT", false)
    )
    Await.result(Future.sequence(seq), 5.seconds)
  }

  behavior of "LoggerService"

  "LoggerService.log" should "create correct log" in {
    val timestamp = Timestamp.from(Instant.now())
    loggerService
      .log(1, timestamp, "IN", true)
      .flatMap(_ => loggerService.getLogs(All))
      .futureValue should contain (Log(1, timestamp, "IN", true))
  }

  "LoggerService.getLogs(All)" should "return all logs" in {
    val logs = loggerService.getLogs(All)
    logs.futureValue should contain (Log(1, timestamp, "IN", true))
    logs.futureValue should contain (Log(2, timestamp, "IN", false))
    logs.futureValue should contain (Log(2, timestamp, "OUT", true))
    logs.futureValue should contain (Log(3, timestamp, "OUT", false))
  }

  "LoggerService.getLogs(In)" should "return in logs" in {
    val logs = loggerService.getLogs(In)
    logs.futureValue should contain (Log(1, timestamp, "IN", true))
    logs.futureValue should contain (Log(2, timestamp, "IN", false))
  }

  "LoggerService.getLogs(Out)" should "return out logs" in {
    val logs = loggerService.getLogs(Out)
    logs.futureValue should contain (Log(2, timestamp, "OUT", true))
    logs.futureValue should contain (Log(3, timestamp, "OUT", false))
  }

  "LoggerService.getLogsByCard" should " " in {
    loggerService.getLogsByCard(1).futureValue should contain (Log(1, timestamp, "IN", true))
    loggerService.getLogsByCard(2).futureValue should contain (Log(2, timestamp, "IN", false))
    loggerService.getLogsByCard(2).futureValue should contain (Log(2, timestamp, "OUT", true))
    loggerService.getLogsByCard(3).futureValue should contain (Log(3, timestamp, "OUT", false))
  }
}