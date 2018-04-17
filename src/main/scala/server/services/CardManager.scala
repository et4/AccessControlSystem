package server.services

import java.util.concurrent.TimeUnit

import slick.jdbc.H2Profile.api._
import server.tables.CardTable
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CardManager {
  def setAccess(cardId: Int, access: Boolean): Unit = {
    val updateQuery = TableQuery[CardTable].filter(_.id == cardId).map(_.hasAccess).update(access)
    Await.result(Database.forConfig("db").run(updateQuery), Duration(2, TimeUnit.SECONDS))
  }

  def setHighPriorityAccess(cardId: Int, access: Boolean): Unit = ???

  def setGroup(cardId: Int, groupId: Int): Unit = {
    val updateQuery = TableQuery[CardTable].filter(_.id == cardId).map(_.groupId).update(groupId)
    Await.result(Database.forConfig("db").run(updateQuery), Duration(2, TimeUnit.SECONDS))
  }

  def removeGroup(cardId: Int, groupId: Int): Unit = {
    val updateQuery = TableQuery[CardTable].filter(_.id == cardId).map(_.groupId).update(null)
    Await.result(Database.forConfig("db").run(updateQuery), Duration(2, TimeUnit.SECONDS))
  }
}
