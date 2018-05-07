package server.tables

import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

final case class Group(
                       id : Option[Int],
                       hasAccess : Boolean,
                     )

final class GroupTable(tag: Tag) extends Table[Group](tag, "GROUP") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def hasAccess = column[Boolean]("HASACCESS")

  def * = (id.?, hasAccess) <> (Group.tupled, Group.unapply)
}