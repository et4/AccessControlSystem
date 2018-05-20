package integration

import server.Card
import server.services.CardServiceImpl
import slick.jdbc.PostgresProfile

class CardServiceTest extends FutureTest with DatabaseTest {
  val cardService = new CardServiceImpl(PostgresProfile)(database)

  behavior of "CardService.hasAccess"

  it should "handle no group cards cases" in {
    cardService.hasAccess(1).futureValue should be (true)
    cardService.hasAccess(2).futureValue should be (false)

  }

  it should "handle cards with one group but without exceptional rules cases" in {
    cardService.hasAccess(3).futureValue should be (true)
    cardService.hasAccess(4).futureValue should be (true)
    cardService.hasAccess(5).futureValue should be (false)
    cardService.hasAccess(6).futureValue should be (false)
  }

  it should "handle cards with two groups but without exceptional rules cases" in {
    cardService.hasAccess(7).futureValue should be (true)
    cardService.hasAccess(8).futureValue should be (true)
    cardService.hasAccess(9).futureValue should be (true)
    cardService.hasAccess(10).futureValue should be (false)
    cardService.hasAccess(11).futureValue should be (false)
    cardService.hasAccess(12).futureValue should be (true)
  }

  it should "handle cards with one group and exceptional rules cases" in {
    cardService.hasAccess(13).futureValue should be (true)
    cardService.hasAccess(14).futureValue should be (false)
    cardService.hasAccess(15).futureValue should be (false)
    cardService.hasAccess(16).futureValue should be (true)
    cardService.hasAccess(17).futureValue should be (true)
    cardService.hasAccess(18).futureValue should be (false)
    cardService.hasAccess(19).futureValue should be (false)
    cardService.hasAccess(20).futureValue should be (true)
  }

  it should "handle cards with two groups and exceptional rules cases" in {
    cardService.hasAccess(21).futureValue should be (true)
    cardService.hasAccess(22).futureValue should be (true)
    cardService.hasAccess(23).futureValue should be (true)
    cardService.hasAccess(24).futureValue should be (true)
  }

  it should "handle cards with defaults and exceptions" in {
    cardService.hasAccess(25).futureValue should be (true)
    cardService.hasAccess(26).futureValue should be (false)
    cardService.hasAccess(27).futureValue should be (true)
  }

  behavior of "CardService.setIndividualAccess"

  it should "change field" in {
    cardService.setIndividualAccess(1, access = false).futureValue should contain (Card(Some(1), false))
    cardService.setIndividualAccess(2, access = true).futureValue should contain (Card(Some(2), true))

    cardService.setIndividualAccess(1, access = true).futureValue should contain (Card(Some(1), true))
    cardService.setIndividualAccess(2, access = false).futureValue should contain (Card(Some(2), false))

  }
}
