package client.view

class ConsoleView {
  def main(args: Array[String]): Unit = {
    showInfo()
    scala.io.StdIn.readInt() match {
      case 1 => showAllKeys()
    }
  }

  def showInfo(): Unit = {
    println("1. Выбрать ключ")
    println("2. Войти")
    println("3. Выйти")
    println("4. Добавить разрешение для ключа")
    println("5. Удалить разрешение для ключа")
    println("6. Добавить разрешение для группы ключей")
    println("7. Добавить исключение для группы ключей")
    println("7. Показать все запросы на вход")
    println("8. Показать все запросы на выход")
    println("9. Выгрузка отчета о ключах, которые в течение времени T совершили более N неуспешных попыток входа")
    println("10. Показать время пребывания сотрудника на работе")
  }

  def showAllKeys(): Unit = {
    //выгрузить из бд
  }


}
