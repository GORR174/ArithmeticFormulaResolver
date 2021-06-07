package net.catstack.polish

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

open class Token(val name: String)

class NumberToken(val value: Float) : Token(value.toString())

class OperatorToken(name: String, val precedence: Int, val action: (Float, Float) -> Float) : Token(name) {
    fun act(a: Float, b: Float) = action(a, b)

    companion object {
        val operators = HashMap<Char, OperatorToken>()

        init {
            registerOperator('+', 2, Float::plus)
            registerOperator('-', 2, Float::minus)
            registerOperator('*', 3, Float::times)
            registerOperator('/', 3, Float::div)
            registerOperator('^', 4, Float::pow)
        }

        fun registerOperator(name: Char, precedence: Int, action: (Float, Float) -> Float) {
            operators[name] = OperatorToken(name.toString(), precedence, action)
        }
    }
}

open class FunctionToken(name: String, val action: (FloatArray) -> Float) : Token(name) {
    fun act(vararg params: Float) = action(params)

    companion object {
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

        fun registerFunction(name: String, action: (FloatArray) -> Float) {
            functions[name] = FunctionToken(name, action)
        }
    }
}

class LeftParenthesis : Token("(")
class RightParenthesis : Token("(")