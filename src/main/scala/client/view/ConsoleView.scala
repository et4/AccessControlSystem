package client.view

import client.domain.{Card, EntranceEvent, ExitEvent}
import client.services.{PermissionService, TurnstileService}

object ConsoleView extends App {

  val turnstileService = new TurnstileService()
  val permissionService = new PermissionService()
  var selectedCardId: Int = -1
  var selectedGroupId: Int = 1

  showInfo()
  println("Select card")
  showAllKeys()
  selectedCardId = scala.io.StdIn.readInt()

  while (true) {
    scala.io.StdIn.readInt() match {
      case 1 => showAllKeys()
      case 2 => enter()
      case 3 => exit()
      case 4 => addPermission()
      case 5 => deletePermission()
      case 6 => addPermissionForGroup()
      case 7 => addExceptionForGroup()
    }
  }

  def showInfo(): Unit = {
    println("1. Select key")
    println("2. Come in")
    println("3. Come out")
    println("4. Add permission for the key")
    println("5. Remove permission for the key")
    println("6. Add permission for a group of keys")
    println("7. Add an exception for a group of keys")
    println("7. Show all come in requests")
    println("8. Show all come out requests")
    println("9. Show report of the keys, which during the time T committed more than N unsuccessful attempts to enter")
    println("10. Show employee's time at work")
  }

  def showAllKeys(): Unit = {
    val pairs: Array[(String, Int)] = turnstileService.requestAllCardsIds().split(";").zipWithIndex
    pairs.foreach(println)
    println("Select card:")
    val cardIndex = scala.io.StdIn.readInt()
    selectedCardId = pairs.find(p => p._2 == cardIndex).get._1.toInt
    println("Selected id:")
    println(selectedCardId)
  }

  def enter(): Unit = {
    turnstileService.requestAccess(selectedCardId, eventType = EntranceEvent())
    printDone()
  }

  def exit(): Unit = {
    turnstileService.requestAccess(selectedCardId, eventType = ExitEvent())
    printDone()
  }

  def addPermission(): Unit = {
    permissionService.setIndividualAccess(selectedCardId, true)
    printDone()
  }

  def deletePermission(): Unit = {
    permissionService.setIndividualAccess(selectedCardId, false)
    printDone()
  }

  def addPermissionForGroup(): Unit = {
    permissionService.setGroupAccess(selectedGroupId, true)
    printDone()
  }

  def addExceptionForGroup(): Unit = {
    permissionService.setExceptionalAccess(selectedCardId, selectedGroupId, "GRANTED")
    printDone()
  }

  def showAllComeInRequests(): Unit = {

  }

  def showAllComeOutRequests(): Unit = {

  }

  def showAnomalies(): Unit = {

  }

  def showTimeAtWork(): Unit = {

  }

  def printDone(): Unit = println("Done")
}
