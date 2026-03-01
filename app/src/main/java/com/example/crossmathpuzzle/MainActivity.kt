package com.example.crossmathpuzzle // Ensure this matches your actual package name

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Material3 theme wrapper
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CrossMathApp()
                }
            }
        }
    }
}

@Composable
fun CrossMathApp() {
    // We use rememberSaveable so if the user rotates the screen,
    // they stay on the same screen they were currently on.
    var currentScreen by rememberSaveable { mutableStateOf("menu") }

    // This handles switching between the menu and the actual game screens
    when (currentScreen) {
        "menu" -> {
            MainMenuScreen(
                onNewGameClick = { currentScreen = "game" },
                onAdvancedClick = { currentScreen = "advanced" }
            )
        }
        "game" -> {
            // Placeholder for Task 3
            // Later we will add logic here to return to menu via Back button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Standard Game Screen")
                Button(onClick = { currentScreen = "menu" }) { Text("Back to Menu") }
            }
        }
        "advanced" -> {
            // Placeholder for Task 8
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Advanced Level Screen")
                Button(onClick = { currentScreen = "menu" }) { Text("Back to Menu") }
            }
        }
    }
}

@Composable
fun MainMenuScreen(onNewGameClick: () -> Unit, onAdvancedClick: () -> Unit) {
    // Simple state to track if the about popup is open
    var showPopup by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Title for the app
        Text(text = "Cross Math Puzzle", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(30.dp))

        // Task 1: The three required buttons
        Button(onClick = onNewGameClick, modifier = Modifier.width(200.dp)) {
            Text("New Game")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = onAdvancedClick, modifier = Modifier.width(200.dp)) {
            Text("Advanced Level")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { showPopup = true }, modifier = Modifier.width(200.dp)) {
            Text("About")
        }
    }

    // Task 2: About popup with the mandatory plagiarism declaration
    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            title = { Text("App Information") },
            text = {
                // Formatting this exactly as the assignment asks
                Column {
                    Text("Author: [Your Name]")
                    Text("Student ID: [Your ID]")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "I confirm that I understand what plagiarism is and have read and " +
                                "understood the section on Assessment Offences in the Essential " +
                                "Information for Students. The work that I have submitted is " +
                                "entirely my own. Any work from other authors is duly referenced " +
                                "and acknowledged."
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPopup = false }) {
                    Text("Close")
                }
            }
        )
    }
}