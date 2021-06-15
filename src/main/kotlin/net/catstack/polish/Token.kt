package net.catstack.polish

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * Представление токена (элемента) польской записи
 * @param name - имя токена для отображения
 */
open class Token(val name: String)

/**
 * Представление числового токена
 * @param value - числовое значение токена
 */
class NumberToken(val value: Float) : Token(value.toString())

/**
 * Класс для представления оператора токена
 * @param name - символ оператора
 * @param precedence - приоритет выполнения оператора
 * @param action - действие при выполнении оператора
 */
class OperatorToken(name: String, val precedence: Int, val action: (Float, Float) -> Float) : Token(name) {
    /**
     * Вызов функции оператора над двумя числами
     * @param a - число слева
     * @param b - число справа
     * @return результат работы оператора
     */
    fun act(a: Float, b: Float) = action(a, b)

    companion object {
        /**
         * Словарь всех операторов
         */
        val operators = HashMap<Char, OperatorToken>()

        init {
            registerOperator('+', 2, Float::plus)
            registerOperator('-', 2, Float::minus)
            registerOperator('*', 3, Float::times)
            registerOperator('/', 3, Float::div)
            registerOperator('^', 4, Float::pow)
        }

        /**
         * Метор для регистрации операторов
         */
        fun registerOperator(name: Char, precedence: Int, action: (Float, Float) -> Float) {
            operators[name] = OperatorToken(name.toString(), precedence, action)
        }
    }
}

/**
 * Класс для представления оператора функции
 * @param name - имя функции
 * @param action - действие при выполнении функции
 */
open class FunctionToken(name: String, val action: (FloatArray) -> Float) : Token(name) {
    /**
     * Вызов функции
     * @param params - список параметров функции
     * @return результат работы функции
     */
    fun act(vararg params: Float) = action(params)

    companion object {
        /**
         * Словарь всех функций
         */
        val functions = HashMap<String, FunctionToken>()

        var input: Float? = null

        init {
            registerFunction("abs") { abs(it[0]) }
            registerFunction("inv") { -it[0] }
            registerFunction("sin") { sin(Math.toRadians(it[0].toDouble())).toFloat() }
            registerFunction("cos") { cos(Math.toRadians(it[0].toDouble())).toFloat() }
            registerFunction("x") {
                if (input == null) {
                    println("Введите x:")
                    input = readLine()!!.toFloat()
                }

                return@registerFunction input!!
            }
        }

        /**
         * Метод для регистрации функции
         */
        fun registerFunction(name: String, action: (FloatArray) -> Float) {
            functions[name] = FunctionToken(name, action)
        }
    }
}

/**
 * Клас для представления открывающей скобки
 */
class LeftParenthesis : Token("(")

/**
 * Клас для представления закрывающей скобки
 */
class RightParenthesis : Token(")")