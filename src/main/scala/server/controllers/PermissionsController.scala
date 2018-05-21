package server.controllers

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, parameters, path, _}
import akka.http.scaladsl.server.Route
import server.services.{CardService, GroupService}

import scala.concurrent.Await


class PermissionsController(cardManager: CardService, groupService: GroupService) extends Controller {
  val routes: Route = {
    path("setIndividualAccess") {
      get {
        parameters('cardId.as[String], 'access.as[Boolean]) { (cardId, access) =>
          onSuccess(cardManager.setIndividualAccess(cardId.toInt, access)) { _ =>
            complete {
              HttpResponse(StatusCodes.OK)
            }
          }
        }
      }
    }
  } ~
    path("setGroupAccess") {
      get {
        parameters('groupId.as[String], 'access.as[Boolean]) { (groupId, access) =>
          onSuccess(groupService.setGroupAccess(groupId.toInt, access)) { _ =>
            complete {
              HttpResponse(StatusCodes.OK)
            }
          }
        }
      }
    } ~
    path("addEmptyGroup") {
      get {
        parameters('access.as[Boolean]) { (access) =>
          onSuccess(groupService.addEmptyGroup(access)) { value =>
            complete {
              HttpResponse(StatusCodes.OK, entity = HttpEntity(value.toString))
            }
          }
        }
      }
    } ~
    path("addGroup") {
      get {
        parameters('cardIds.as[String], 'access.as[Boolean]) { (cardIds, access) =>
          //id передаются в виде строки через ;
          onSuccess(groupService.createGroupForCards(cardIds.split(";").map(_.toInt).toSeq, access)) { value =>
            complete {
              HttpResponse(StatusCodes.OK, entity = HttpEntity(value.toString()))
            }
          }
        }
      }
    } ~
    path("kickFromGroup") {
      get {
        parameters('cardId.as[String], 'groupId.as[String]) { (cardId, groupId) =>
          onSuccess(groupService.kickFromGroup(cardId.toInt, groupId.toInt)) { _ =>
            complete {
              HttpResponse(StatusCodes.OK)
            }
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
    } ~
    path("getAllCards") {
      get {
        complete {
          val cards = Await.result(cardManager.getAllCards, timeout).map(card => card.id.get.toString).mkString(";")
          HttpResponse(StatusCodes.OK, entity = HttpEntity(cards))
        }
      }
    }
}
