package com.movil.iosCalculator.calculator.view


import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.movil.iosCalculator.calculator.viewModel.CalculatorViewModel


@Composable
fun CalculatorButton(
    text: String,
    type: ButtonType,
    viewModel: CalculatorViewModel,
    isWide: Boolean = false
) {

    // El botón decide su color según el tipo
    val backgroundColor = when (type) {
        ButtonType.NUMBER -> Color(0xFF333333)
        ButtonType.OPERATOR -> Color(0xFFFF9500)
        ButtonType.ACTION -> Color(0xFFA5A5A5)
    }

    Button(
        onClick = { viewModel.manageClickButtons(text) },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        modifier = Modifier
            .size(
                width = if (isWide) 180.dp else 88.dp,
                height = 88.dp
            )
    ) {
        Text(
            text = text,
            fontSize = 28.sp,
            color = Color.White
        )
    }
}

