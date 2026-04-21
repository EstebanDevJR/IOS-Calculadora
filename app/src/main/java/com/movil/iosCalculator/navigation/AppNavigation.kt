package com.movil.iosCalculator.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.movil.iosCalculator.calculator.view.CalculatorScreen
import com.movil.iosCalculator.history.view.historyScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "calculator"
    ) {
        composable("calculator") {
            CalculatorScreen(navController = navController)
        }

        composable("history") {
            historyScreen(navController = navController)
        }
    }
}
