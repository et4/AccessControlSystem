import java.util.Calendar

import client._

import scala.util.Random

object Simulation extends App {
  val turn = new Turnstile()

  val cards: Seq[Card] = generateRandomCards(10)

  cards.foreach(card => turn.getAccsess(card, Calendar.getInstance.getTime, EntranceEvent()))

  def generateRandomCards(amount: Int): Seq[Card] = {
    val MAX_ID = 1000000
    for (id <- Seq.fill(amount)(Random.nextInt(MAX_ID))) yield new Card(id, getRandomPermission)
  }

  def getRandomPermission: Permission = {
    val randomValue = Random.nextInt(3)
    if (randomValue == 0) PermissionDefault()
    else if (randomValue == 1) PermissionPresent()
    else PermissionAbsent()
  }
}
