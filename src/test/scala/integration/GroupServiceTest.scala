package integration

import server.{Group, GroupAccess}
import server.services.GroupServiceImpl
import slick.jdbc.PostgresProfile
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await

class GroupServiceTest extends FutureTest with DatabaseTest {
  val groupService = new GroupServiceImpl(PostgresProfile)(database)

  behavior of "GroupService.addEmptyGroup"

  "GroupService.addEmptyGroup" should "create correct group" in {
    groupService
      .addEmptyGroup(true).futureValue should be (Group(Some(5), hasAccess = true))

    groupService
      .addEmptyGroup(false).futureValue should be (Group(Some(6), hasAccess = false))
  }

  "GroupService.createGroupForCards" should "create group access for every card" in {
    val eventualAccesses = groupService.createGroupForCards(Seq(1, 2, 3, 4), false)
    val groupId = Await.result(eventualAccesses.map(_.head.groupId), 5.seconds)
    eventualAccesses.futureValue should contain (GroupAccess(1, groupId, "DEFAULT"))
    eventualAccesses.futureValue should contain (GroupAccess(2, groupId, "DEFAULT"))
    eventualAccesses.futureValue should contain (GroupAccess(3, groupId, "DEFAULT"))
    eventualAccesses.futureValue should contain (GroupAccess(4, groupId, "DEFAULT"))
  }

  "GroupService.createGroupForCards" should "create group access for every card" in {
    groupService
      .setExceptionalAccess(3, 1, "FORBIDDEN")
      .flatMap(_ => groupService.getGroupAccess(3, 1))
      .futureValue should be (GroupAccess(3,1,"FORBIDDEN"))

    groupService
      .setExceptionalAccess(3, 1, "GRANTED")
      .flatMap(_ => groupService.getGroupAccess(1, 1))
      .futureValue should be (GroupAccess(3,1,"GRANTED"))

    groupService
      .setExceptionalAccess(3, 1, "DEFAULT")
      .flatMap(_ => groupService.getGroupAccess(3, 1))
      .futureValue should be (GroupAccess(3,1,"DEFAULT"))
  }
}
