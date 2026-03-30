package com.example.calculadoraios.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculadoraios.calculator.CalculatorViewModel

sealed class Screen(val route: String) {
    object Calculator : Screen("calculator")
    object History : Screen("history")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: CalculatorViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Calculator.route
    ) {
        composable(Screen.Calculator.route) {
            CalculatorScreen(
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                viewModel = viewModel
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }
}
