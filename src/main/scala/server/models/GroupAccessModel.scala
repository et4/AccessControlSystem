package server.models

import server.GroupAccess

trait GroupAccessModel extends DatabaseModel with CardModel with GroupModel {
  import profile.api._

  final class GroupAccessTable(tag: Tag) extends Table[GroupAccess](tag, "groupaccess") {
    def cardId = column[Int]("cardid")

    def groupId = column[Int]("groupid")

    def access = column[String]("exceptionalaccess")

    def * = (cardId, groupId, access) <> (GroupAccess.tupled, GroupAccess.unapply)

    def card = foreignKey("fk_GroupAccess_cardId", cardId, TableQuery[CardTable])(_.id)

    def group = foreignKey("fk_GroupAccess_groupId", groupId, TableQuery[GroupTable])(_.id)
  }

  val groupsAccess = TableQuery[GroupAccessTable]
}