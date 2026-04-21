package com.movil.iosCalculator.calculator.view
import androidx.compose.material3.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.movil.iosCalculator.calculator.viewModel.CalculatorViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    navController: NavController

) { val result = viewModel.result.collectAsState().value
    val resultScrollState = rememberScrollState()
    LaunchedEffect(result) {
        resultScrollState.scrollTo(resultScrollState.maxValue)
    }
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) { Button(onClick = {navController.navigate("history")},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFFA70E)
        ),
        modifier = Modifier
            .padding(start = 1.dp, top = 10.dp)
            .size(60.dp)

    ){
        Text(
            text = "≡",
            fontSize = 35.sp
        )

    }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // 👈 CLAVE
                .padding(horizontal = 18.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom // 👈 lo baja dentro de su espacio
        ) {
            Text(
                text = result,
                color = Color.White,
                fontSize = 80.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.horizontalScroll(resultScrollState)
            )
        }


        // Usamos filas (Rows) dentro de una columna para organizar los botones
        val spacing = 12.dp

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // FILA 1: AC, +/-, %, /
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                CalculatorButton("AC", ButtonType.ACTION, viewModel)
                CalculatorButton("+/-", ButtonType.ACTION, viewModel)
                CalculatorButton("%", ButtonType.ACTION,viewModel)
                CalculatorButton("÷", ButtonType.OPERATOR, viewModel)
            }

            // FILA 2: 7, 8, 9, X
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                CalculatorButton("7", ButtonType.NUMBER, viewModel)
                CalculatorButton("8", ButtonType.NUMBER, viewModel)
                CalculatorButton("9", ButtonType.NUMBER, viewModel)
                CalculatorButton("×", ButtonType.OPERATOR, viewModel)
            }

            // FILA 3: 4, 5, 6, -
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                CalculatorButton("4", ButtonType.NUMBER,viewModel)
                CalculatorButton("5", ButtonType.NUMBER, viewModel)
                CalculatorButton("6", ButtonType.NUMBER,viewModel)
                CalculatorButton("-", ButtonType.OPERATOR,viewModel)
            }

            // FILA 4: 1, 2, 3, +
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                CalculatorButton("1", ButtonType.NUMBER, viewModel)
                CalculatorButton("2", ButtonType.NUMBER, viewModel)
                CalculatorButton("3", ButtonType.NUMBER, viewModel)
                CalculatorButton("+", ButtonType.OPERATOR, viewModel)
            }

            // FILA 5: 0, decimal, =
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                // El botón "0" usa isWide = true
                CalculatorButton("0", ButtonType.NUMBER, viewModel, isWide = true)
                CalculatorButton(",", ButtonType.NUMBER,viewModel)
                CalculatorButton("=", ButtonType.OPERATOR, viewModel)
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TestView() {
    val navController = rememberNavController()
    CalculatorScreen(
        navController = navController
    )
}


