package net.catstack.polish

import java.lang.RuntimeException

/**
 * Класс для представления обратной польской записи. В конструктор передаётся формула в инфиксной нотации,
 * которая будет преобразована в обратную польскую запись.
 * @param formula - формула в инфиксной нотации
 */
class PolishNotation(formula: String) {
    val notation = arrayListOf<Token>()
    val operators = ArrayDeque<Token>()

    private var previousToken: Token? = null

    init {
        val numberBuilder = StringBuilder()
        val functionBuilder = StringBuilder()

        for (symbol in formula) {
            symbol.processSymbolAsNumber(numberBuilder)
            symbol.processSymbolAsFunction(functionBuilder)
            symbol.processSymbolAsOperator()
            symbol.processSymbolAsParenthesis()
        }

        ' '.processSymbolAsNumber(numberBuilder)
        ' '.processSymbolAsFunction(functionBuilder)

        while (operators.size > 0) {
            if (operators.last().name == "(" || operators.last().name == ")")
                throw RuntimeException("Формула имеет неверный формат!")
            notation.add(operators.removeLast())
        }
    }

    /**
     * Метод решения формулы
     */
    fun resolve(): Float {
        val stack = ArrayDeque<Float>()

        for (token in notation) {
            when (token) {
                is NumberToken -> stack.addLast(token.value)
                is OperatorToken -> {
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    stack.addLast(token.act(a, b))
                }
                is FunctionToken -> stack.addLast(token.act(stack.removeLast()))
            }
        }

        return stack.removeLast()
    }

    /**
     * Преобразование формулы в строку
     */
    override fun toString() = notation.joinToString(" ") { it.name }

    /**
     * Обработка символа, как число
     * @param numberBuilder - StringBuilder, который собирает число
     */
    private fun Char.processSymbolAsNumber(numberBuilder: StringBuilder) {
        if (this.isDigit() || this == '.')
            numberBuilder.append(this)
        else if (numberBuilder.isNotEmpty()) {
            val numberToken = NumberToken(numberBuilder.toString().toFloat())
            notation.add(numberToken)
            previousToken = numberToken

            numberBuilder.clear()
        }
    }

    /**
     * Обработка символа, как функции
     * @param functionBuilder - StringBuilder, который собирает функцию
     */
    private fun Char.processSymbolAsFunction(functionBuilder: StringBuilder) {
        if (this.isLetter())
            functionBuilder.append(this)
        else if (functionBuilder.toString() == "x") {
            val num = FunctionToken.functions["x"]!!.act()
            val numberToken = NumberToken(num)
            notation.add(numberToken)
            previousToken = numberToken
            functionBuilder.clear()
        } else if (functionBuilder.isNotEmpty()) {
            val functionToken = FunctionToken.functions[functionBuilder.toString()]
            operators.addLast(functionToken!!)
            previousToken = functionToken

            functionBuilder.clear()
        }
    }

    /**
     * Обработка символа, как оператора
     */
    private fun Char.processSymbolAsOperator() {
        if (this == '.' || this == ' ' || this == '(' || this == ')' || this.isLetterOrDigit())
            return

        if (this == '-' && (previousToken == null || previousToken is LeftParenthesis || previousToken is OperatorToken)) {
            val functionToken = FunctionToken.functions["inv"]!!
            operators.addLast(functionToken)
            previousToken = functionToken
        } else {
            val operatorToken = OperatorToken.operators[this]

            while (operators.size != 0 && (operators.last() is FunctionToken ||
                        operators.last() is OperatorToken &&
                        (operators.last() as OperatorToken).precedence >= operatorToken!!.precedence)
            )
                notation.add(operators.removeLast())

            operators.addLast(operatorToken!!)
            previousToken = operatorToken
        }
    }

    /**
     * Обработка символа, как скобки
     */
    private fun Char.processSymbolAsParenthesis() {
        when (this) {
            '(' -> {
                operators.addLast(LeftParenthesis())
                previousToken = LeftParenthesis()
            }
            ')' -> {
                while (operators.last() !is LeftParenthesis)
                    notation.add(operators.removeLast())

                if (operators.last() is LeftParenthesis)
                    operators.removeLast()

                if (operators.size > 0 && operators.last() is FunctionToken)
                    notation.add(operators.removeLast())

                previousToken = RightParenthesis()
            }
        }
    }
}