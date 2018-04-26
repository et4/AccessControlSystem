package server.tables

import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

final case class UserGroup(
                       id : Option[Int],
                       hasAccess : Boolean
                     )

final class UserGroupTable(tag: Tag) extends Table[UserGroup](tag, "USERGROUP") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def hasAccess = column[Boolean]("HASACCESS")

  def * = (id.?, hasAccess) <> (UserGroup.tupled, UserGroup.unapply)
}