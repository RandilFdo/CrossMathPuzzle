package com.example.crossmathpuzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CrossMathApp()
                }
            }
        }
    }
}

@Composable
fun CrossMathApp() {
    var currentScreen by rememberSaveable { mutableStateOf("menu") }

    when (currentScreen) {
        "menu" -> MainMenuScreen(
            onNewGameClick = { currentScreen = "game" },
            onAdvancedClick = { currentScreen = "advanced" }
        )
        "game" -> GameScreen(onBack = { currentScreen = "menu" })
        "advanced" -> Text("Coming soon...")
    }
}

@Composable
fun GameScreen(onBack: () -> Unit, vm: GameViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) { Text("Back") }
            Text("Score: 0", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Task 5: Use a grid to display the puzzle
        LazyVerticalGrid(
            columns = GridCells.Fixed(vm.gridSize),
            modifier = Modifier.weight(1).border(1.dp, Color.Gray)
        ) {
            items(vm.gridSize * vm.gridSize) { index ->
                val row = index / vm.gridSize
                val col = index % vm.gridSize
                val cellText = vm.gridData["$row,$col"] ?: ""
                val isEditable = vm.editableCells.contains("$row,$col")

                // Task 4: Determine color feedback (Red if wrong, Green if right, White if empty)
                val backgroundColor = when {
                    !isEditable -> Color.Transparent
                    cellText.isEmpty() -> Color.LightGray
                    vm.isCorrect(row, col) -> Color.Green
                    else -> Color.Red
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(0.5.dp, Color.LightGray)
                        .background(backgroundColor)
                        .clickable(enabled = isEditable) {
                            // We will add the number input popup here in the next step
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = cellText, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun MainMenuScreen(onNewGameClick: () -> Unit, onAdvancedClick: () -> Unit) {
    var showPopup by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cross Math Puzzle", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onNewGameClick, modifier = Modifier.width(200.dp)) { Text("New Game") }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onAdvancedClick, modifier = Modifier.width(200.dp)) { Text("Advanced Level") }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { showPopup = true }, modifier = Modifier.width(200.dp)) { Text("About") }
    }

    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            title = { Text("App Information") },
            text = {
                Column {
                    Text("Author: [Your Name]")
                    Text("Student ID: [Your ID]")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.")
                }
            },
            confirmButton = { TextButton(onClick = { showPopup = false }) { Text("Close") } }
        )
    }
}