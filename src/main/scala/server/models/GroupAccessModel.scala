package server.models

trait GroupAccessModel extends DatabaseModel with CardModel with GroupModel {
  import profile.api._

  final case class GroupAccess(
                                cardId: Int,
                                groupId: Int,
                                exceptionalAccess: String
                              )

  final class GroupAccessTable(tag: Tag) extends Table[GroupAccess](tag, "GROUPACCESS") {
    def cardId = column[Int]("CARDID")

    def groupId = column[Int]("GROUPID")

    def access = column[String]("EXCEPTIONALACCESS")

    def * = (cardId, groupId, access) <> (GroupAccess.tupled, GroupAccess.unapply)

    def card = foreignKey("fk_GroupAccess_cardId", cardId, TableQuery[CardTable])(_.id)

    def group = foreignKey("fk_GroupAccess_groupId", groupId, TableQuery[GroupTable])(_.id)
  }

  val groupsAccess = TableQuery[GroupAccessTable]
}