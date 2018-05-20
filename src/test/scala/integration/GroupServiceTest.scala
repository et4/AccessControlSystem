package integration

import server.Group
import server.services.GroupServiceImpl
import slick.jdbc.PostgresProfile

class GroupServiceTest extends FutureTest with DatabaseTest {
  val groupService = new GroupServiceImpl(PostgresProfile)(database)

  behavior of "GroupService.addEmptyGroup"

  it should "create correct group" in {
    groupService
      .addEmptyGroup(true).futureValue should be (Group(Some(5), hasAccess = true))

    groupService
      .addEmptyGroup(false).futureValue should be (Group(Some(6), hasAccess = false))
  }

  it should " " in {

  }
}
