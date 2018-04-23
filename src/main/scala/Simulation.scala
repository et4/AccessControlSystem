import java.time.Instant

import client._

import scala.util.Random

object Simulation extends App {
  val turn = new Turnstile()

  val cards: Seq[Card] = generateRandomCards(10)

  cards.foreach(card => turn.requestAccess(card, Instant.now(), randomEvent))

  def generateRandomCards(amount: Int): Seq[Card] = {
    val MAX_ID = 1000000
    for (id <- Seq.fill(amount)(Random.nextInt(MAX_ID))) yield Card(id, randomPermission)
  }

  def randomPermission: Permission = {
    val randomValue = Random.nextInt(3)
    if (randomValue == 0) PermissionDefault
    else if (randomValue == 1) PermissionPresent
    else PermissionAbsent
  }

  def randomEvent: TurnstileEvent = {
    if (Random.nextInt(2) == 0) EntranceEvent()
    else ExitEvent()
  }
}
