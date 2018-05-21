package client.view

import java.sql.Timestamp

import client.domain.{Card, EntranceEvent, ExitEvent}
import client.services.{LogService, PermissionService, TurnstileService}

object ConsoleView extends App {

  val turnstileService = new TurnstileService()
  val permissionService = new PermissionService()
  val logService = new LogService()
  var selectedCardId: Int = -1
  var selectedGroupId: Int = 1

  showInfo()
  showAllKeys()

  while (true) {
    println("Select item:")
    scala.io.StdIn.readInt() match {
      case 1 => showAllKeys()
      case 2 => enter()
      case 3 => exit()
      case 4 => addPermission()
      case 5 => deletePermission()
      case 6 => addPermissionForGroup()
      case 7 => addExceptionForGroup()
      case 8 => showAllComeInRequests()
      case 9 => showAllComeOutRequests()
    }
    printDone()
  }

  def showInfo(): Unit = {
    println("1. Select key")
    println("2. Come in")
    println("3. Come out")
    println("4. Add permission for the key")
    println("5. Remove permission for the key")
    println("6. Add permission for a group of keys")
    println("7. Add an exception for a group of keys")
    println("8. Show all come in requests")
    println("9. Show all come out requests")
    println("10. Show report of the keys, which during the time T committed more than N unsuccessful attempts to enter")
    println("11. Show employee's time at work")
  }

  def showAllKeys(): Unit = {
    val pairs: Array[(String, Int)] = turnstileService.requestAllCardsIds().split(";").zipWithIndex
    println("All cards:")
    pairs.foreach(println)
    println("Choose index:")
    val cardIndex = scala.io.StdIn.readInt()
    selectedCardId = pairs.find(p => p._2 == cardIndex).get._1.toInt
    println("Selected id:")
    println(selectedCardId)
  }

  def enter(): Unit = {
    turnstileService.requestAccess(selectedCardId, eventType = EntranceEvent())
  }

  def exit(): Unit = {
    turnstileService.requestAccess(selectedCardId, eventType = ExitEvent())
  }

  def addPermission(): Unit = {
    permissionService.setIndividualAccess(selectedCardId, true)
  }

  def deletePermission(): Unit = {
    permissionService.setIndividualAccess(selectedCardId, false)
  }

  def addPermissionForGroup(): Unit = {
    permissionService.setGroupAccess(selectedGroupId, true)
  }

  def addExceptionForGroup(): Unit = {
    permissionService.setExceptionalAccess(selectedCardId, selectedGroupId, "FORBIDDEN")
  }

  def showAllComeInRequests(): Unit = {
    logService.getComeInLogs.foreach(println)
  }

  def showAllComeOutRequests(): Unit = {
    logService.getComeOutLogs.foreach(println)
  }

  def showAnomalies(): Unit = {
    //    logService.getAnomalies()
  }

  def showTimeAtWork(): Unit = {
    //    logService.getTimeInside()
  }

  def printDone(): Unit = println("Done")
}
