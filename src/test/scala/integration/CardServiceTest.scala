package integration

import server.services.CardServiceImpl
import slick.jdbc.PostgresProfile

class CardServiceTest extends FutureTest with DatabaseTest {
  val cardService = new CardServiceImpl(PostgresProfile)(database)

  behavior of "CardService"

  "UserService.hasAccess" should "handle all cases" in {
    cardService.hasAccess(1).futureValue should be (true)
    cardService.hasAccess(2).futureValue should be (false)
    cardService.hasAccess(3).futureValue should be (true)
    cardService.hasAccess(4).futureValue should be (true)
    cardService.hasAccess(5).futureValue should be (false)
    cardService.hasAccess(6).futureValue should be (false)
    cardService.hasAccess(7).futureValue should be (false)
    cardService.hasAccess(8).futureValue should be (true)
  }
}
