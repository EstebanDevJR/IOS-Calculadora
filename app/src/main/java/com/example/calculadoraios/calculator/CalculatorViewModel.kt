package com.example.calculadoraios.calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.calculadoraios.history.HistoryViewModel

class CalculatorViewModel(
    val historyViewModel: HistoryViewModel
) : ViewModel() {

    private val model = CalculatorModel()
    val result = mutableStateOf("0")
    var contentResult = mutableListOf("0")
    private var isCalculated = false
    
    private var lastOperation = ""
    private var lastNumber = ""
    private var justCalculated = false

    val history: androidx.compose.runtime.State<List<Pair<String, String>>>
        get() = historyViewModel.history

    fun showContent() {
        val display = contentResult.joinToString("")
        val hasComma = display.contains(",")
        val formatted = if (hasComma) display else display.replace(".", ",")
        result.value = if (formatted.isNotEmpty()) formatted else "0"
    }

    private fun isPrimaryOperator(s: String): Boolean = s == "÷" || s == "×" || s == "+" || s == "-" || s == "%"

    private fun isOperator(s: String): Boolean = isPrimaryOperator(s)

    fun manageClickButtons(valueButton: String) {
        if (valueButton.isEmpty()) return

        when (valueButton) {
            "AC" -> resetCalculator()
            "⁺/₋" -> toggleSign()
            "%" -> handlePercentage()
            "÷", "×", "+" -> handlePrimaryOperator(valueButton)
            "-" -> handleMinus()
            "=" -> calculateResult()
            "," -> handleComma()
            else -> handleNumber(valueButton)
        }
        showContent()
    }

    private fun resetCalculator() {
        contentResult.clear()
        contentResult.add("0")
        result.value = "0"
        isCalculated = false
        lastOperation = ""
        lastNumber = ""
        justCalculated = false
    }

    private fun toggleSign() {
        val lastIndex = contentResult.size - 1
        if (lastIndex >= 0) {
            val last = contentResult[lastIndex]
            if (!isOperator(last)) {
                val toggled = if (last.startsWith("-")) {
                    if (last.length > 1) last.substring(1) else last
                } else {
                    "-$last"
                }
                contentResult[lastIndex] = toggled
            }
        }
        isCalculated = false
    }

    private fun handlePercentage() {
        if (isCalculated) {
            val currentVal = parseNumber(contentResult)
            if (currentVal != null) {
                val percentage = currentVal / 100
                contentResult.clear()
                contentResult.add(formatResult(percentage))
                lastOperation = ""
                lastNumber = ""
            }
            return
        }

        val opIndex = contentResult.indexOfLast { isPrimaryOperator(it) }
        
        if (opIndex > 0 && opIndex < contentResult.size - 1) {
            val num1 = parseNumber(contentResult.subList(0, opIndex))
            val operator = contentResult[opIndex]
            val num2 = parseNumber(contentResult.subList(opIndex + 1, contentResult.size))
            
            if (num1 != null && num2 != null) {
                val result = when (operator) {
                    "+" -> num1 + (num1 * num2 / 100)
                    "-" -> num1 - (num1 * num2 / 100)
                    "×" -> num1 * num2 / 100
                    "÷" -> num1 / (num2 / 100)
                    else -> num1 / 100
                }
                contentResult.clear()
                contentResult.add(formatResult(result))
                isCalculated = true
            }
            return
        }

        val lastIndex = contentResult.size - 1
        val last = contentResult.getOrNull(lastIndex)
        if (last != null && !isOperator(last)) {
            val currentVal = last.toDoubleOrNull()
            if (currentVal != null) {
                contentResult[lastIndex] = formatResult(currentVal / 100)
            }
        }
    }

    private fun handlePrimaryOperator(operator: String) {
        val last = contentResult.lastOrNull()
        when {
            last == null -> {}
            isPrimaryOperator(last) -> contentResult[contentResult.size - 1] = operator
            last == "-" -> {}
            else -> contentResult.add(operator)
        }
        isCalculated = false
    }

    private fun handleMinus() {
        val last = contentResult.lastOrNull()
        when {
            last == null -> contentResult.add("-")
            last == "-" -> {}
            last == "+" || last == "×" || last == "÷" || last == "%" || last == "+/-" -> contentResult.add("-")
            isPrimaryOperator(last) -> contentResult.add("-")
            else -> contentResult.add("-")
        }
        isCalculated = false
    }

    private fun handleComma() {
        if (isCalculated) {
            contentResult.clear()
            contentResult.add("0.")
            isCalculated = false
            return
        }

        val hasComma = contentResult.any { it.contains(".") }
        if (hasComma) return

        val lastIndex = contentResult.size - 1
        if (lastIndex >= 0) {
            val last = contentResult[lastIndex]
            when {
                isOperator(last) -> contentResult.add("0.")
                last.contains(".") -> {}
                else -> contentResult[lastIndex] = "$last."
            }
        } else {
            contentResult.add("0.")
        }
    }

    private fun handleNumber(num: String) {
        if (result.value == "Error") {
            contentResult.clear()
            contentResult.add(num)
            isCalculated = false
            return
        }

        if (isCalculated) {
            contentResult.clear()
            contentResult.add(num)
            isCalculated = false
            return
        }

        val lastIndex = contentResult.size - 1
        val last = contentResult.getOrNull(lastIndex)

        when {
            last == null || (isOperator(last) && last != "-") -> contentResult.add(num)
            last == "-" -> contentResult[lastIndex] = last + num
            last == "0" -> contentResult[lastIndex] = num
            last.length >= 15 || (last.contains(".") && last.length >= 17) -> {}
            else -> contentResult[lastIndex] = last + num
        }
    }

    fun calculateResult() {
        val parsed = parseExpression()
        if (parsed == null) {
            if (lastOperation.isNotEmpty() && lastNumber.isNotEmpty() && isCalculated) {
                repeatLastOperation()
            }
            return
        }

        val (num1, operator, num2) = parsed

        val calculatedResult = performOperation(num1, num2, operator)

        if (calculatedResult == null) {
            result.value = "Error"
            isCalculated = true
            return
        }

        val resultStr = formatResult(calculatedResult)
        val expression = "$num1 ${getOperationDisplay(operator)} $num2"
        
        historyViewModel.addOperation(expression, resultStr)

        contentResult.clear()
        contentResult.add(resultStr)
        
        lastOperation = operator
        lastNumber = num2.toString()
        isCalculated = true
        justCalculated = true
    }

    private fun parseExpression(): Triple<Double, String, Double>? {
        if (contentResult.size < 2) return null

        val operatorIndex = contentResult.indexOfLast { isPrimaryOperator(it) }
        
        if (operatorIndex <= 0 || operatorIndex >= contentResult.size - 1) {
            return null
        }

        val num1Part = contentResult.subList(0, operatorIndex)
        val num2Part = contentResult.subList(operatorIndex + 1, contentResult.size)

        val num1 = parseNumber(num1Part) ?: return null
        val num2 = parseNumber(num2Part) ?: return null

        return Triple(num1, contentResult[operatorIndex], num2)
    }

    private fun parseNumber(parts: List<String>): Double? {
        if (parts.isEmpty()) return null
        
        val combined = parts.joinToString("")
        if (combined.isEmpty()) return null
        
        return combined.toDoubleOrNull()
    }

    private fun repeatLastOperation() {
        if (lastOperation.isEmpty() || lastNumber.isEmpty()) return
        
        val currentVal = parseNumber(contentResult) ?: return
        val num2 = lastNumber.toDoubleOrNull() ?: return

        val calculatedResult = performOperation(currentVal, num2, lastOperation)

        if (calculatedResult == null) {
            result.value = "Error"
            isCalculated = true
            return
        }

        val resultStr = formatResult(calculatedResult)
        val expression = "$currentVal ${getOperationDisplay(lastOperation)} $lastNumber"
        
        if (!justCalculated) {
            historyViewModel.addOperation(expression, resultStr)
        }

        contentResult.clear()
        contentResult.add(resultStr)
        
        isCalculated = true
        justCalculated = false
    }

    private fun getOperationDisplay(op: String): String = when (op) {
        "*" -> "×"
        "/" -> "÷"
        else -> op
    }

    private fun toOperatorSymbol(displaySymbol: String): String {
        return when (displaySymbol) {
            "×" -> "*"
            "÷" -> "/"
            else -> displaySymbol
        }
    }

    private fun performOperation(num1: Double, num2: Double, op: String): Double? {
        val symbol = toOperatorSymbol(op)
        return when (symbol) {
            "+" -> model.add(num1, num2)
            "-" -> model.subtract(num1, num2)
            "*" -> model.multiply(num1, num2)
            "/" -> if (num2 == 0.0) null else model.divide(num1, num2)
            "%" -> model.percentage(num1, num2)
            else -> null
        }
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble() && result < Long.MAX_VALUE) {
            result.toLong().toString()
        } else {
            String.format("%.10f", result).trimEnd('0').trimEnd('.')
        }
    }

    fun clearHistory() {
        historyViewModel.clearHistory()
    }
}