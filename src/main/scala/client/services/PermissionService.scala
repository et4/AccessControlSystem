package client.services

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.Uri.Query
import scala.concurrent.Future

class PermissionService extends Service {
  def setIndividualAccess(cardId: Int, access: Boolean): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/setIndividualAccess").withQuery(Query(
        "cardId" -> cardId.toString,
        "access" -> access.toString))))

  def setGroupAccess(groupId: Int, access: Boolean): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/setGroupAccess").withQuery(Query(
        "groupId" -> groupId.toString,
        "access" -> access.toString))))

  def addEmptyGroup(access: Boolean): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/addEmptyGroup").withQuery(Query(
        "access" -> access.toString))))

  def addGroup(cardIds: String, access: Boolean): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/addGroup").withQuery(Query(
        "cardIds" -> cardIds,
        "access" -> access.toString))))

  def kickFromGroup(cardId: Int, groupId: Int): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/kickFromGroup").withQuery(Query(
        "cardId" -> cardId.toString,
        "groupId" -> groupId.toString))))

  def setExceptionalAccess(cardId: Int, groupId: Int, access: String): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/setExceptionalAccess").withQuery(Query(
        "cardId" -> cardId.toString,
        "groupId" -> groupId.toString,
        "access" -> access))))

  def setGroupToCard(cardId: Int, groupId: Int): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/setGroupToCard").withQuery(Query(
        "cardId" -> cardId.toString,
        "groupId" -> groupId.toString))))

  def setGroupToCards(cardIds: String, groupId: Int): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(HttpMethods.GET,
      Uri(uri + "/setGroupToCards").withQuery(Query(
        "cardIds" -> cardIds,
        "groupId" -> groupId.toString))))
}
