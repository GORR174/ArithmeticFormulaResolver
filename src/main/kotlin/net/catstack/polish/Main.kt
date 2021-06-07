package net.catstack.polish

fun main() {
    while (true) {
        println("Введите формулу:")
        val input = readLine()!!

        while (true) {
            val polishNotation = PolishNotation(input)

            println("Обратная польская запись: $polishNotation")
            println("Результат: " + "%.2f".format(polishNotation.resolve()))

            if (FunctionToken.input == null) {
                break
            }

            FunctionToken.input = null

            println("Что делать дальше?\n[1] - Ввести новую формулу\n[2] - изменить значение переменной")

            when (readLine()!!) {
                "1" -> break
                "2" -> println("Формула: $input")
            }
        }

        println("----------------------")
    }
}