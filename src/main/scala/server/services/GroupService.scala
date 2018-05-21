package server.services

import server.models._
import server.{Group, GroupAccess}
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait GroupService {
  def addEmptyGroup(access: Boolean): Future[Group]

  def setGroupAccess(groupId: Int, access: Boolean): Future[Group]

  def setExceptionalAccess(cardId: Int, groupId: Int, access: String): Future[GroupAccess]

  def setGroupToCard(cardId: Int, groupId: Int): Future[GroupAccess]

  def kickFromGroup(cardId: Int, groupId: Int): Future[Int]

  def createGroupForCards(cardsId: Seq[Int], access: Boolean): Future[Seq[GroupAccess]]

  def setGroupToCards(cardsId: Seq[Int], groupId: Int): Future[Seq[GroupAccess]]

  def getGroup(groupId: Int): Future[Group]

  def getGroups: Future[Seq[Group]]

  def getGroupAccess(cardId: Int, groupId: Int): Future[GroupAccess]

  def getGroupsAccess: Future[Seq[GroupAccess]]
}

class GroupServiceImpl(val profile: JdbcProfile)(implicit db: JdbcBackend.Database)
  extends GroupService with GroupModel with GroupAccessModel {

  import profile.api._

  def getGroups: Future[Seq[Group]] = {
    db.run(groups.result)
  }

  def getGroup(groupId: Int): Future[Group] = {
    db.run(groups.filter(_.id === groupId.bind).result.head)
  }

  def getGroupAccess(cardId: Int, groupId: Int): Future[GroupAccess] = {
    db.run(
      groupsAccess
        .filter(_.groupId === groupId.bind)
        .filter(_.cardId === cardId.bind)
        .result
        .head
    )
  }

  def getGroupsAccess(cardId: Int, groupId: Int): Future[Seq[GroupAccess]] = {
    db.run(groupsAccess.result)
  }

  def addEmptyGroup(access: Boolean): Future[Group] = {
    db.run(
      (groups returning groups.map(_.id) into ((group, id) => group.copy(id = Some(id)))) += Group(None, access)
    )
  }

  def createGroupForCards(cardsId: Seq[Int], access: Boolean): Future[Seq[GroupAccess]] = {
    val futureGroup: Future[Group] = addEmptyGroup(access)
    futureGroup.map(
      group =>
        setGroupToCards(
          cardsId,
          group.id.getOrElse(throw new SlickException("Something bad happened"))
        )
    ).flatten
  }

  def setGroupAccess(groupId: Int, access: Boolean): Future[Group] = {
    db.run(
      groups
        .filter(_.id === groupId.bind)
        .map(_.hasAccess)
        .update(access)
        .andThen(
          groups
            .filter(_.id === groupId.bind)
            .result
            .head
        )
    )
  }

  def setExceptionalAccess(cardId: Int, groupId: Int, access: String): Future[GroupAccess] = {
    db.run(
      groupsAccess
        .filter(_.groupId === groupId.bind)
        .filter(_.cardId === cardId.bind)
        .map(_.access)
        .update(access)
    ).map[GroupAccess](_ => GroupAccess(cardId, groupId, access))
  }

  def setGroupToCard(cardId: Int, groupId: Int): Future[GroupAccess] = {
    val groupAccess = GroupAccess(cardId, groupId, "DEFAULT")
    db.run(
      groupsAccess.insertOrUpdate(groupAccess)
    ).map[GroupAccess](_ => groupAccess)
  }

  def setGroupToCards(cardsId: Seq[Int], groupId: Int): Future[Seq[GroupAccess]] = {
    Future.traverse(cardsId)(setGroupToCard(_, groupId))
  }

  def kickFromGroup(cardId: Int, groupId: Int): Future[Int] = {
    db.run(
      groupsAccess
        .filter(_.groupId === groupId.bind)
        .filter(_.cardId === cardId.bind)
        .delete
    )
  }
}
