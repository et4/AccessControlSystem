package server.controllers

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, parameters, path, _}
import akka.http.scaladsl.server.Route
import server.services.{CardService, GroupService}


class PermissionsController(cardManager: CardService, groupService: GroupService) extends Controller {
  val routes: Route = {
    path("setIndividualAccess") {
      get {
        parameters('cardId.as[String], 'access.as[Boolean]) { (cardId, access) =>
          complete {
            cardManager.setIndividualAccess(cardId.toInt, access)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    }
  } ~
    path("setGroupAccess") {
      get {
        parameters('groupId.as[String], 'access.as[Boolean]) { (groupId, access) =>
          complete {
            groupService.setGroupAccess(groupId.toInt, access)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("addEmptyGroup") {
      get {
        parameters('access.as[Boolean]) { (access) =>
          complete {
            groupService.addEmptyGroup(access)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("addGroup") {
      get {
        parameters('cardIds.as[String], 'access.as[Boolean]) { (cardIds, access) =>
          complete {
            //id передаются в виде строки через ;
            groupService.addGroup(cardIds.split(";").map(_.toInt).toSeq, access)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("kickFromGroup") {
      get {
        parameters('cardId.as[String], 'groupId.as[String]) { (cardId, groupId) =>
          complete {
            groupService.kickFromGroup(cardId.toInt, groupId.toInt)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("setExceptionalAccess") {
      get {
        parameters('cardId.as[String], 'groupId.as[String], 'access.as[String]) { (cardId, groupId, access) =>
          complete {
            groupService.setExceptionalAccess(cardId.toInt, groupId.toInt, access)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("setGroupToCard") {
      get {
        parameters('cardId.as[String], 'groupId.as[String]) { (cardId, groupId) =>
          complete {
            groupService.setGroupToCard(cardId.toInt, groupId.toInt)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    } ~
    path("setGroupToCards") {
      get {
        parameters('cardIds.as[String], 'groupId.as[String]) { (cardIds, groupId) =>
          complete {
            groupService.setGroupToCards(cardIds.split(";").map(_.toInt).toSeq, groupId.toInt)
            HttpResponse(StatusCodes.OK)
          }
        }
      }
    }
}
