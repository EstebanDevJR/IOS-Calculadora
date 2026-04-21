package com.movil.iosCalculator.calculator.model

class CalculatorModel {

    fun add(num1: Double, num2: Double): Double = num1 + num2

    fun subtract(num1: Double, num2: Double): Double = num1 - num2

    fun multiply(num1: Double, num2: Double): Double = num1 * num2

    fun divide(num1: Double, num2: Double): Double {
        if (num2 == 0.0) {
            throw ArithmeticException("División por cero")
        }
        return num1 / num2
    }

    fun module(num1: Double, num2: Double): Double {
        if (num2 == 0.0) {
            throw ArithmeticException("Módulo por cero")
        }
        return num1 % num2
    }

    fun calculate(num1: Double, num2: Double, operator: String): Double {
        return when (operator) {
            "+" -> add(num1, num2)
            "-" -> subtract(num1, num2)
            "×" -> multiply(num1, num2)
            "÷" -> divide(num1, num2)
            "%" -> module(num1, num2)
            else -> throw IllegalArgumentException("Operador inválido: $operator")
        }
    }
    fun evaluateExpression(tokens: List<String>): Double? {
        if (tokens.isEmpty()) return null
        if (tokens.size == 1) return tokens[0].toDoubleOrNull()

        val list = tokens.toMutableList()

        return try {


            var i = 1
            while (i < list.size) {
                val token = list[i]
                val prev = list[i - 1]
                if (token.startsWith("-") && token.length > 1 && prev.toDoubleOrNull() != null) {
                    list[i] = token.substring(1)
                    list.add(i, "-")
                    i += 2
                    continue
                }
                i++
            }
            i = 1
            while (i < list.size - 1) {
                val op = list[i]
                if (op == "×" || op == "÷" || op == "%") {
                    val left = list[i - 1].toDoubleOrNull() ?: return null
                    val right = list[i + 1].toDoubleOrNull() ?: return null
                    val result = calculate(left, right, op)
                    list[i - 1] = result.toString()
                    list.removeAt(i + 1)
                    list.removeAt(i)
                    i = 0
                    continue
                }
                i++
            }

            i = 1
            while (i < list.size - 1) {
                val op = list[i]
                if (op == "+" || op == "-") {
                    val left = list[i - 1].toDoubleOrNull() ?: return null
                    val right = list[i + 1].toDoubleOrNull() ?: return null
                    val result = calculate(left, right, op)
                    list[i - 1] = result.toString()
                    list.removeAt(i + 1)
                    list.removeAt(i)
                    i = 0
                    continue
                }
                i++
            }

            list.firstOrNull()?.toDouble()

        } catch (e: Exception) {
            null
        }
    }
}


