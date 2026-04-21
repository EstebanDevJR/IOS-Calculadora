package com.movil.iosCalculator.calculator.viewModel

import androidx.lifecycle.ViewModel
import com.movil.iosCalculator.calculator.model.CalculatorModel
import com.movil.iosCalculator.history.model.HistoryItem
import com.movil.iosCalculator.history.model.HistoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel () : ViewModel() {

    private val model = CalculatorModel()

    private val _result = MutableStateFlow("0")
    val result: StateFlow<String> = _result.asStateFlow()

    private val _previousOperation = MutableStateFlow("")
    val previousOperation: StateFlow<String> = _previousOperation.asStateFlow()

    private var tokens = mutableListOf<String>()
    private var isCalculated = false
    private var savedExpression = ""
    private fun cleanNumberFormat(number: String): String {
        return when {
            number == "-0" -> "0"
            number.endsWith(".0") -> number.substringBefore(".0")
            else -> number
        }
    }

    private fun formatDisplay(value: String): String {
        var formatted = value
        if (formatted.endsWith(".0")) {
            formatted = formatted.substringBefore(".0")
        }
        return formatted.replace(".", ",")
    }

    private fun formatNumberForDisplay(number: Double): String {
        val formatted = if (number == number.toLong().toDouble()) {
            number.toLong().toString()
        } else {
            "%.8f".format(number).trimEnd('0').trimEnd('.')
        }
        return formatted.replace(".", ",")
    }

    private fun isOperator(s: String): Boolean =
        s == "÷" || s == "×" || s == "-" || s == "+" || s == "%"

    private fun isErrorState(): Boolean =
        tokens.size == 1 && tokens.firstOrNull() == "Error"

    private fun buildExpressionForDisplay(tokenList: List<String>): String {
        if (tokenList.isEmpty()) return ""

        val expression = StringBuilder()
        var i = 0

        while (i < tokenList.size) {
            val token = tokenList[i]

            if (token == "-" && (i == 0 || isOperator(tokenList[i - 1]))) {
                if (i + 1 < tokenList.size) {
                    expression.append("-${tokenList[i + 1]}")
                    i += 2
                    continue
                }
            }

            if (expression.isNotEmpty() && !isOperator(token) && !token.startsWith("-")) {
                expression.append(" ")
            }

            expression.append(token)

            if (!isOperator(token) && i < tokenList.size - 1) {
                expression.append(" ")
            }

            i++
        }

        return expression.toString()
    }

    private fun updateDisplay() {

        _result.value = if (tokens.isNotEmpty()) {
            val raw = tokens.joinToString("")
            formatDisplay(cleanNumberFormat(raw))
        } else {
            "0"
        }

        _previousOperation.value = when {
            isCalculated && savedExpression.isNotEmpty() -> savedExpression
            else -> buildExpressionForDisplay(tokens)
        }
    }



    private fun calculateResult() {

        if (tokens.isEmpty()) return

        if (isOperator(tokens.last())) {
            tokens.removeAt(tokens.lastIndex)
        }
        android.util.Log.d("CALC_TOKENS", "Tokens antes de evaluar: $tokens")
        savedExpression = tokens.joinToString(" ")
        val resultValue = model.evaluateExpression(tokens)

        if (resultValue != null) {

            val formatted = formatNumberForDisplay(resultValue)
            HistoryModel.records.add(
                HistoryItem(expression = savedExpression, result = formatted)
            )
            tokens.clear()
            tokens.add(formatted.replace(",", "."))
            isCalculated = true

        } else {
            tokens.clear()
            tokens.add("Error")
        }

        updateDisplay()
    }
    fun manageClickButtons(valueButton: String) {

        if (isErrorState()) {
            when (valueButton) {
                "AC" -> {
                    tokens.clear()
                    isCalculated = false
                    savedExpression = ""
                    updateDisplay()
                }
                ".", "," -> {
                    tokens.clear()
                    tokens.add("0.")
                    isCalculated = false
                    savedExpression = ""
                    updateDisplay()
                }
                else -> if (valueButton.length == 1 && valueButton.first().isDigit()) {
                    tokens.clear()
                    tokens.add(valueButton)
                    isCalculated = false
                    savedExpression = ""
                    updateDisplay()
                }
            }
            return
        }

        when (valueButton) {

            "AC" -> {
                tokens.clear()
                isCalculated = false
                savedExpression = ""
            }

            "+/-" -> {
                if (tokens.isEmpty()) return

                val lastIndex = tokens.size - 1
                val last = tokens[lastIndex]

                if (!isOperator(last)) {
                    val num = last.toDoubleOrNull()
                    if (num != null) {
                        val toggled = (-num).toString()
                        tokens[lastIndex] = cleanNumberFormat(toggled)
                    }
                }

                isCalculated = false
                savedExpression = ""
            }
            "%", "÷", "×", "+","-" -> {

                if (tokens.isEmpty()) return

                val last = tokens.last()

                if (isOperator(last)) {
                    tokens[tokens.size - 1] = valueButton
                } else {
                    if (isCalculated) {
                        val res = tokens.last()
                        tokens.clear()
                        tokens.add(res)
                        tokens.add(valueButton)
                        isCalculated = false
                        savedExpression = ""
                    } else {
                        tokens.add(valueButton)
                    }
                }
            }
            "=" -> {
                calculateResult()
                return
            }
            ".", "," -> {

                if (tokens.isEmpty()) {
                    tokens.add("0.")
                } else {
                    val last = tokens.last()

                    if (isOperator(last)) {
                        tokens.add("0.")
                    } else {
                        if (!last.contains(".")) {
                            tokens[tokens.size - 1] = "$last."
                        }
                    }
                }

                isCalculated = false
                savedExpression = ""
            }

            else -> {

                if (isCalculated) {
                    tokens.clear()
                    tokens.add(valueButton)
                    isCalculated = false
                    savedExpression = ""
                }
                else if (tokens.isEmpty()) {
                    tokens.add(valueButton)
                }
                else {

                    val last = tokens.last()

                    when {

                        last == "-" -> {
                            tokens[tokens.size - 1] = "-$valueButton"
                        }

                        isOperator(last) -> {
                            tokens.add(valueButton)
                        }

                        last == "0" -> {
                            tokens[tokens.size - 1] = valueButton
                        }

                        last == "-0" -> {
                            tokens[tokens.size - 1] = "-$valueButton"
                        }

                        else -> {
                            tokens[tokens.size - 1] = last + valueButton
                        }
                    }
                }
            }
        }

        updateDisplay()
    }
}
