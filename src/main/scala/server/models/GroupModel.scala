package server.models

trait GroupModel extends DatabaseModel {
  import profile.api._

  final case class Group(
                          id: Option[Int],
                          hasAccess: Boolean,
                        )

  final class GroupTable(tag: Tag) extends Table[Group](tag, "group") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def hasAccess = column[Boolean]("hasaccess")

    def * = (id.?, hasAccess) <> (Group.tupled, Group.unapply)
  }

  val groups = TableQuery[GroupTable]
}