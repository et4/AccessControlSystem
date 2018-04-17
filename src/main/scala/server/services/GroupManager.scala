package server.services

import slick.jdbc.H2Profile.api._
import java.sql.Time
import java.util.concurrent.TimeUnit

import server.tables.{Log, LogTable, UserGroup, UserGroupTable}
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object GroupManager {
  def addEmptyGroup(access: Boolean): Int = {
    val groups = TableQuery[UserGroupTable]
    val actionWithGroupId = (groups returning groups.map(_.id)) += UserGroup(None, access)
    Await.result(Database.forConfig("db").run(actionWithGroupId), Duration(2, TimeUnit.SECONDS))
  }

  def addGroup(usersId: Seq[Int], access: Boolean): Unit = {
    val groupId = addEmptyGroup(access)
    usersId.foreach(CardManager.setGroup(_, groupId))
  }
}
