package com.example.calculadoraios.calculator

class CalculatorModel {

    fun add(num1: Double, num2: Double): Double {
        return num1 + num2
    }

    fun subtract(num1: Double, num2: Double): Double {
        return num1 - num2
    }

    fun multiply(num1: Double, num2: Double): Double {
        return num1 * num2
    }

    fun divide(num1: Double, num2: Double): Double? {
        return if (num2 != 0.0) num1 / num2 else null
    }

    fun percentage(num1: Double, num2: Double? = null): Double {
        return if (num2 != null) {
            num1 * (num2 / 100.0)
        } else {
            num1 / 100.0
        }
    }
}
