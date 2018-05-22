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

    val seq: Seq[Future[Log]] = Seq(
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

  "LoggerService.getLogsByCard" should "should show all possible logs for card" in {
    loggerService.getLogsByCard(1, All).futureValue should contain (Log(1, timestamp, "IN", true))
    loggerService.getLogsByCard(2, All).futureValue should contain (Log(2, timestamp, "IN", false))
    loggerService.getLogsByCard(2, All).futureValue should contain (Log(2, timestamp, "OUT", true))
    loggerService.getLogsByCard(3, All).futureValue should contain (Log(3, timestamp, "OUT", false))
  }

  "LoggerService.getAnomalies" should "filter correct" in {
    val logs = Future.sequence(Seq(
      loggerService.log(9, new Timestamp(1000), "IN", false),
      loggerService.log(9, new Timestamp(1000), "IN", false),
      loggerService.log(9, new Timestamp(1000), "IN", false),
      loggerService.log(9, new Timestamp(1000), "IN", false),
      loggerService.log(10, new Timestamp(1000), "IN", false),
      loggerService.log(10, new Timestamp(1000), "IN", false),
      loggerService.log(10, new Timestamp(1000), "IN", false),
      loggerService.log(10, new Timestamp(1000), "IN", false),
      loggerService.log(10, new Timestamp(1000), "IN", false)
    ))
    logs.flatMap(_ => loggerService.getAnomalies(new Timestamp(0), new Timestamp(10000), 5)).futureValue should not contain (9)
    logs.flatMap(_ => loggerService.getAnomalies(new Timestamp(0), new Timestamp(10000), 5)).futureValue should contain (10)
  }

  "LoggerService.getTimeInside" should "calculate correct time for simple case" in {
    Future.sequence(Seq(
    loggerService.log(11, new Timestamp(105), "IN", true),
    loggerService.log(11, new Timestamp(110), "OUT", true),
    loggerService.log(11, new Timestamp(113), "IN", true),
    loggerService.log(11, new Timestamp(115), "OUT", true)))
      .flatMap(
        _ => loggerService.getTimeInside(11, new Timestamp(100), new Timestamp(120))
      ).futureValue should be(7L)

  }

  "LoggerService.getTimeInside" should "calculate correct time for no bound1 case" in {
    loggerService
      .log(12, new Timestamp(105), "IN", true)
      .flatMap(_ =>
            loggerService
              .getTimeInside(12, new Timestamp(100), new Timestamp(120))
      ).futureValue should be (15L)

  }

  "LoggerService.getTimeInside" should "calculate correct time for no bound2 case" in {
    loggerService
      .log(13, new Timestamp(105), "OUT", true)
      .flatMap(_ => loggerService
          .getTimeInside(13, new Timestamp(100), new Timestamp(120))
      ).futureValue should be (5L)
  }
}