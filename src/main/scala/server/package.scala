import java.sql.Timestamp

package object server {
  final case class Card(id : Option[Int], hasAccess : Boolean)
  final case class GroupAccess(cardId: Int, groupId: Int, exceptionalAccess: String)
  final case class Group(id: Option[Int], hasAccess: Boolean)
  final case class Log(cardId: Int, dateTime: Timestamp, eventType: String, success: Boolean)
}
