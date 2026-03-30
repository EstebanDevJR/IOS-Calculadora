package com.example.calculadoraios.calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    private val model = CalculatorModel()
    val result = mutableStateOf("0")
    var contentResult = mutableListOf("0")
    private var isCalculated = false
    
    private var lastOperation = ""
    private var lastNumber = ""
    private var justCalculated = false

    val history = mutableStateOf<List<Pair<String, String>>>(emptyList())

    fun showContent() {
        result.value = if (contentResult.isNotEmpty()) {
            contentResult.joinToString("").replace(".", ",")
        } else {
            "0"
        }
    }

    private fun isOperator(s: String): Boolean = s == "÷" || s == "×" || s == "-" || s == "+" || s == "%"

    fun manageClickButtons(valueButton: String) {
        if (valueButton.isEmpty()) return

        when (valueButton) {
            "AC" -> {
                contentResult.clear()
                contentResult.add("0")
                isCalculated = false
                lastOperation = ""
                lastNumber = ""
                justCalculated = false
            }
            "⁺/₋" -> {
                val lastIndex = contentResult.size - 1
                if (lastIndex >= 0) {
                    val last = contentResult[lastIndex]
                    if (!isOperator(last)) {
                        val currentVal = last.toDoubleOrNull() ?: 0.0
                        if (currentVal != 0.0) {
                            val toggled = if (last.startsWith("-")) last.substring(1) else "-$last"
                            contentResult[lastIndex] = toggled
                        }
                    }
                }
                isCalculated = false
            }
            "%", "÷", "×", "-", "+" -> {
                val displayOp = when (valueButton) {
                    "÷" -> "/"
                    "×" -> "*"
                    else -> valueButton
                }
                val last = contentResult.lastOrNull()
                if (last != null) {
                    if (isOperator(last)) {
                        contentResult[contentResult.size - 1] = valueButton
                    } else {
                        contentResult.add(valueButton)
                    }
                }
                isCalculated = false
            }
            "=" -> {
                calculateResult()
            }
            "," -> {
                if (isCalculated) {
                    contentResult.clear()
                    contentResult.add("0.")
                    isCalculated = false
                } else {
                    val lastIndex = contentResult.size - 1
                    if (lastIndex >= 0) {
                        val last = contentResult[lastIndex]
                        if (!isOperator(last)) {
                            if (!last.contains(".")) {
                                contentResult[lastIndex] = "$last."
                            }
                        } else {
                            contentResult.add("0.")
                        }
                    } else {
                        contentResult.add("0.")
                    }
                }
            }
            else -> {
                if (isCalculated) {
                    contentResult.clear()
                    contentResult.add(valueButton)
                    isCalculated = false
                } else {
                    val lastIndex = contentResult.size - 1
                    if (lastIndex < 0 || isOperator(contentResult[lastIndex])) {
                        contentResult.add(valueButton)
                    } else {
                        val last = contentResult[lastIndex]
                        if (last == "0") {
                            contentResult[lastIndex] = valueButton
                        } else {
                            contentResult[lastIndex] = last + valueButton
                        }
                    }
                }
            }
        }
        showContent()
    }

    fun calculateResult() {
        if (contentResult.size < 3) {
            if (lastOperation.isNotEmpty() && lastNumber.isNotEmpty() && isCalculated) {
                repeatLastOperation()
            }
            return
        }

        val operatorIndex = contentResult.indexOfFirst { isOperator(it) }
        if (operatorIndex == -1 || operatorIndex == 0 || operatorIndex == contentResult.size - 1) {
            if (lastOperation.isNotEmpty() && lastNumber.isNotEmpty() && isCalculated) {
                repeatLastOperation()
            }
            return
        }

        val num1Str = contentResult.subList(0, operatorIndex).joinToString("")
        val operation = contentResult[operatorIndex]
        val num2Str = contentResult.subList(operatorIndex + 1, contentResult.size).joinToString("")

        val num1 = num1Str.toDoubleOrNull() ?: return
        val num2 = num2Str.toDoubleOrNull() ?: return

        val expression = "$num1Str ${getOperationDisplay(operation)} $num2Str"
        
        val calculatedResult = performOperation(num1, num2, operation) ?: return

        val resultStr = formatResult(calculatedResult)
        
        val historyList = history.value.toMutableList()
        historyList.add(0, Pair(expression, resultStr))
        history.value = historyList

        contentResult.clear()
        contentResult.add(resultStr)
        
        lastOperation = operation
        lastNumber = num2Str
        isCalculated = true
        justCalculated = true
    }

    private fun repeatLastOperation() {
        if (lastOperation.isEmpty() || lastNumber.isEmpty()) return
        
        val currentVal = contentResult.joinToString("")
        val expression = "$currentVal ${getOperationDisplay(lastOperation)} $lastNumber"
        
        val num1 = currentVal.toDoubleOrNull() ?: return
        val num2 = lastNumber.toDoubleOrNull() ?: return

        val calculatedResult = performOperation(num1, num2, lastOperation) ?: return

        val resultStr = formatResult(calculatedResult)
        
        if (!justCalculated) {
            val historyList = history.value.toMutableList()
            historyList.add(0, Pair(expression, resultStr))
            history.value = historyList
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

    private fun performOperation(num1: Double, num2: Double, op: String): Double? {
        return when (op) {
            "+" -> model.add(num1, num2)
            "-" -> model.subtract(num1, num2)
            "*" -> model.multiply(num1, num2)
            "/" -> if (num2 == 0.0) null else model.divide(num1, num2)
            "%" -> model.percentage(num1, num2)
            else -> null
        }
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            String.format("%.8f", result).trimEnd('0').trimEnd('.')
        }
    }

    fun clearHistory() {
        history.value = emptyList()
    }
}
