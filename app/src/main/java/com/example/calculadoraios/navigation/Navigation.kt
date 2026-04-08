package com.example.calculadoraios.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculadoraios.calculator.CalculatorViewModel
import com.example.calculadoraios.history.HistoryViewModel

sealed class Screen(val route: String) {
    object Calculator : Screen("calculator")
    object History : Screen("history")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val historyViewModel: HistoryViewModel = viewModel()
    val calculatorViewModel: CalculatorViewModel = CalculatorViewModel(historyViewModel)

    NavHost(
        navController = navController,
        startDestination = Screen.Calculator.route
    ) {
        composable(Screen.Calculator.route) {
            _root_ide_package_.com.example.calculadoraios.ui.CalculatorScreen(
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                },
                viewModel = calculatorViewModel
            )
        }
        composable(Screen.History.route) {
            _root_ide_package_.com.example.calculadoraios.history.HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = historyViewModel
            )
        }
    }
}
