package server.models

import server.Group

trait GroupModel extends DatabaseModel {
  import profile.api._

  final class GroupTable(tag: Tag) extends Table[Group](tag, "group") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def hasAccess = column[Boolean]("hasaccess")

    def * = (id.?, hasAccess) <> (Group.tupled, Group.unapply)
  }

  val groups = TableQuery[GroupTable]
}