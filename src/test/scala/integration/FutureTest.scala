package integration

import org.scalatest.{FlatSpecLike, Matchers}
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.duration._

trait FutureTest extends FlatSpecLike with Matchers with ScalaFutures {
  override implicit def patienceConfig: PatienceConfig = PatienceConfig(10.seconds, 100.millis)
}
