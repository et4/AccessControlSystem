package server.tables

import java.util.concurrent.TimeUnit

import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

import scala.concurrent.Await
import scala.concurrent.duration.Duration

final case class UserGroup(
                       id : Option[Int],
                       hasAccess : Boolean,
                     )

final class UserGroupTable(tag: Tag) extends Table[UserGroup](tag, "USERGROUP") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def hasAccess = column[Boolean]("HASACCESS")

  def * = (id, hasAccess).mapTo[UserGroup]
}

private object DbTest3 extends App {
  val cards = TableQuery[UserGroupTable]
  val db = Database.forConfig("db")
  def exec[T](action: DBIO[T]): T =
    Await.result(db.run(action), Duration(2, TimeUnit.SECONDS))

  exec(cards.result)
}