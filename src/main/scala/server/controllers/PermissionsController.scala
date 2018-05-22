package server.controllers

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, parameters, path, _}
import akka.http.scaladsl.server.Route
import server.services.{CardService, GroupService}


class PermissionsController(cardManager: CardService, groupService: GroupService) extends Controller {
  val routes: Route = {
    path("setIndividualAccess") {
      get {
        parameters('cardId.as[String], 'access.as[Boolean]) { (cardId, access) =>
          onSuccess(cardManager.setIndividualAccess(cardId.toInt, access)) { value =>
            complete {
              value match {
                case Some(_) => HttpResponse(StatusCodes.OK)
                case None => HttpResponse(StatusCodes.NotFound)
              }
            }
          }
        }
      }
    }
  } ~
    path("setGroupAccess") {
      get {
        parameters('groupId.as[String], 'access.as[Boolean]) { (groupId, access) =>
          onSuccess(groupService.setGroupAccess(groupId.toInt, access)) { value =>
            complete {
              value match {
                case Some(_) => HttpResponse(StatusCodes.OK)
                case None => HttpResponse(StatusCodes.NotFound)
              }
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
              HttpResponse(StatusCodes.OK, entity = HttpEntity(value.id.get.toString))
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
          onSuccess(groupService.kickFromGroup(cardId.toInt, groupId.toInt)) { value =>
            complete {
              value match {
                case v if v != 0 => HttpResponse(StatusCodes.OK)
                case v if v == 0 => HttpResponse(StatusCodes.NotFound)
              }

            }
          }
        }
      }
    } ~
    path("setExceptionalAccess") {
      get {
        parameters('cardId.as[String], 'groupId.as[String], 'access.as[String]) { (cardId, groupId, access) =>
          onSuccess(groupService.setExceptionalAccess(cardId.toInt, groupId.toInt, access)) { value =>
            complete {
              value match {
                case Some(_) => HttpResponse(StatusCodes.OK)
                case None => HttpResponse(StatusCodes.NotFound)
              }

            }
          }
        }
      }
    } ~
    path("setGroupToCard") {
      get {
        parameters('cardId.as[String], 'groupId.as[String]) { (cardId, groupId) =>
          onSuccess(groupService.setGroupToCard(cardId.toInt, groupId.toInt)) { value =>
            complete {
              value match {
                case Some(_) => HttpResponse (StatusCodes.OK)
                case None => HttpResponse (StatusCodes.NotFound)
              }
            }
          }
        }
      }
    } ~
    path("setGroupToCards") {
      get {
        parameters('cardIds.as[String], 'groupId.as[String]) { (cardIds, groupId) =>
          onSuccess(groupService.setGroupToCards(cardIds.split(";").map(_.toInt).toSeq, groupId.toInt)) { _ =>
            complete {
              HttpResponse(StatusCodes.OK)
            }
          }
        }
      }
    } ~
    path("createGroupForCards") {
      get {
        parameters('cardIds.as[String], 'access.as[Boolean]) { (cardIds, access) =>
          onSuccess(groupService.createGroupForCards(cardIds.split(";").map(_.toInt).toSeq, access)) { value =>
            complete {
              value.flatten.headOption match {
                case Some(ga) => HttpResponse(StatusCodes.OK, entity = HttpEntity(ga.groupId.toString))
                case None => HttpResponse(StatusCodes.NotFound)
              }
            }
          }
        }
      }
    } ~
    path("getAllCards") {
      get {
        onSuccess(cardManager.getAllCards) { cards =>
          complete {
            HttpResponse(
              StatusCodes.OK,
              entity = HttpEntity(cards.map(card => card.id.get.toString).mkString(";"))
            )
          }
        }
      }
    }
}
