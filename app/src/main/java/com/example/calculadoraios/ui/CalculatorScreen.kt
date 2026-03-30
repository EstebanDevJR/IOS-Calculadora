package com.example.calculadoraios.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculadoraios.calculator.CalculatorViewModel

@Composable
fun CalculatorScreen(
    onNavigateToHistory: () -> Unit,
    viewModel: CalculatorViewModel = viewModel()
) {
    val currentNumber by viewModel.result
    val scrollState = rememberScrollState()

    LaunchedEffect(currentNumber) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        Button(
            onClick = onNavigateToHistory,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFFFFA70E)
            ),
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
                .size(60.dp)
        ) {
            Text(
                text = "≡",
                fontSize = 35.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = currentNumber,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White,
                    maxLines = 1,
                    modifier = Modifier.horizontalScroll(scrollState),
                    textAlign = TextAlign.End
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "AC",
                    backgroundColor = Color(0xFFA5A5A5),
                    textColor = Color.Black,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("AC") }
                )
                CalculatorButton(
                    text = "+/-",
                    backgroundColor = Color(0xFFA5A5A5),
                    textColor = Color.Black,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("⁺/₋") }
                )
                CalculatorButton(
                    text = "%",
                    backgroundColor = Color(0xFFA5A5A5),
                    textColor = Color.Black,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("%") }
                )
                CalculatorButton(
                    text = "÷",
                    backgroundColor = Color(0xFFFF9500),
                    textColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("÷") }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "7",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("7") }
                )
                CalculatorButton(
                    text = "8",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("8") }
                )
                CalculatorButton(
                    text = "9",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("9") }
                )
                CalculatorButton(
                    text = "×",
                    backgroundColor = Color(0xFFFF9500),
                    textColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("×") }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "4",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("4") }
                )
                CalculatorButton(
                    text = "5",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("5") }
                )
                CalculatorButton(
                    text = "6",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("6") }
                )
                CalculatorButton(
                    text = "-",
                    backgroundColor = Color(0xFFFF9500),
                    textColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("-") }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "1",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("1") }
                )
                CalculatorButton(
                    text = "2",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("2") }
                )
                CalculatorButton(
                    text = "3",
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("3") }
                )
                CalculatorButton(
                    text = "+",
                    backgroundColor = Color(0xFFFF9500),
                    textColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.manageClickButtons("+") }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "0",
                    modifier = Modifier
                        .weight(2f)
                        .height(80.dp),
                    isZero = true,
                    onClick = { viewModel.manageClickButtons("0") }
                )
                CalculatorButton(
                    text = ".",
                    modifier = Modifier.height(80.dp),
                    onClick = { viewModel.manageClickButtons(",") }
                )
                CalculatorButton(
                    text = "=",
                    backgroundColor = Color(0xFFFF9500),
                    textColor = Color.White,
                    modifier = Modifier.height(80.dp),
                    onClick = { viewModel.manageClickButtons("=") }
                )
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF1C1C1E),
    textColor: Color = Color.White,
    isZero: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = if (isZero) RoundedCornerShape(40.dp) else CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
