package com.movil.iosCalculator.history.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.movil.iosCalculator.history.model.HistoryModel

@Composable
fun historyScreen(navController: NavController) {
    val history = HistoryModel.records

    Scaffold(
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFFFFA70E)
                    )
                ) {
                    Text("‹ Volver", fontSize = 18.sp)
                }

                if (history.isNotEmpty()) {
                    Button(
                        onClick = { HistoryModel.records.clear() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFFFFA70E)
                        )
                    ) {
                        Text("Borrar", fontSize = 18.sp)
                    }
                }
            }

            LazyColumn {
                items(history) { item ->
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.expression,
                                fontSize = 18.sp,
                                color = Color(0xFF8E8E93)
                            )
                            Text(
                                text = "= ${item.result}",
                                fontSize = 20.sp,
                                color = Color(0xFFFFA70E)
                            )
                        }
                        HorizontalDivider(color = Color(0xFF2C2C2E), modifier = Modifier.padding(top = 10.dp))
                    }
                }
            }
        }
    }
}
