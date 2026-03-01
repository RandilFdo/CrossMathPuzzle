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
import kotlinx.coroutines.delay

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
        "advanced" -> Text("Advanced mode logic goes here...")
    }
}

@Composable
fun GameScreen(onBack: () -> Unit, vm: GameViewModel = viewModel()) {
    // State for the number input popup (Task 6)
    var showInputPopup by remember { mutableStateOf(false) }
    var activeCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var tempInput by remember { mutableStateOf("") }

    // Timer Logic (Task 9)
    var isTimerEnabled by rememberSaveable { mutableStateOf(false) }
    var timeLeft by rememberSaveable { mutableStateOf(60) }
    var isGameOver by rememberSaveable { mutableStateOf(false) }

    // This effect runs the countdown when the switch is ON
    LaunchedEffect(key1 = isTimerEnabled, key2 = timeLeft) {
        if (isTimerEnabled && timeLeft > 0 && !isGameOver) {
            delay(1000L)
            timeLeft--
            if (timeLeft == 0) isGameOver = true
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        // Top Bar: Back Button, Timer Switch, and Score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onBack) { Text("Back") }

            // Task 9: Timer Switch
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Timer: ")
                Switch(checked = isTimerEnabled, onCheckedChange = { isTimerEnabled = it })
                if (isTimerEnabled) {
                    Text("  ${timeLeft}s", color = if (timeLeft < 10) Color.Red else Color.Black)
                }
            }

            Text("Score: ${vm.score.value}", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (isGameOver) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("GAME OVER!", style = MaterialTheme.typography.displayMedium, color = Color.Red)
            }
        } else {
            // Task 5: The Puzzle Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(vm.gridSize),
                modifier = Modifier.weight(1f).border(1.dp, Color.Black)
            ) {
                items(vm.gridSize * vm.gridSize) { index ->
                    val row = index / vm.gridSize
                    val col = index % vm.gridSize
                    val cellValue = vm.gridData["$row,$col"] ?: ""
                    val isEditable = vm.editableCells.contains("$row,$col")

                    // Task 4: Color feedback (Green if correct, Red if wrong)
                    val bgColor = when {
                        !isEditable -> Color.White
                        cellValue.isEmpty() -> Color(0xFFE0E0E0) // Light Gray for empty
                        vm.isCellCorrect(row, col) -> Color.Green
                        else -> Color.Red
                    }

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .border(0.5.dp, Color.Gray)
                            .background(bgColor)
                            .clickable(enabled = isEditable) {
                                activeCell = Pair(row, col)
                                tempInput = cellValue
                                showInputPopup = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = cellValue, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    // Task 6: Input Dialog for entering numbers
    if (showInputPopup && activeCell != null) {
        AlertDialog(
            onDismissRequest = { showInputPopup = false },
            title = { Text("Enter Number") },
            text = {
                TextField(
                    value = tempInput,
                    onValueChange = { if (it.all { char -> char.isDigit() }) tempInput = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    vm.updateCell(activeCell!!.first, activeCell!!.second, tempInput)
                    showInputPopup = false
                }) { Text("Confirm") }
            }
        )
    }
}

// Keep your MainMenuScreen code here...
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