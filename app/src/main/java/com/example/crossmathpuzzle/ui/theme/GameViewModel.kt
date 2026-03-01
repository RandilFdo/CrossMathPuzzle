package com.example.crossmathpuzzle

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.random.Random

// This ViewModel keeps our game data alive even if the phone is rotated.
// It handles the "brain" of the app: generating puzzles and checking answers.
class GameViewModel : ViewModel() {

    // Task 3: The grid size must be random between 11 and 20
    var gridSize = Random.nextInt(11, 21)

    // This tracks the user's score (1 point per correct equation)
    var score = mutableStateOf(0)

    // A map to store the content of each cell. Key is "row,col"
    var gridData = mutableStateMapOf<String, String>()

    // Set of keys for cells the user is allowed to edit
    val editableCells = mutableSetOf<String>()

    // Hidden map to store the correct answers for comparison
    private val correctAnswers = mutableMapOf<String, String>()

    init {
        setupNewGame()
    }

    fun setupNewGame() {
        gridData.clear()
        editableCells.clear()
        correctAnswers.clear()
        score.value = 0

        // Let's populate the grid with some random equations
        // We'll place them every 3 rows to keep the UI clean for now
        for (r in 0 until gridSize step 3) {
            generateHorizontalEquation(r)
        }
    }

    private fun generateHorizontalEquation(row: Int) {
        val ops = listOf("+", "-", "*", "/")
        val op = ops.random()

        val val1 = Random.nextInt(1, 15)
        val val2 = Random.nextInt(1, 15)

        // Calculate the result
        val result = when (op) {
            "+" -> val1 + val2
            "-" -> val1 - val2
            "*" -> val1 * val2
            else -> if (val2 != 0) val1 / val2 else 0
        }

        // Column positions
        val col1 = 1
        val colOp = 2
        val col2 = 3
        val colEq = 4
        val colRes = 5

        // Fill the grid
        gridData["$row,$col1"] = val1.toString()
        gridData["$row,$colOp"] = op

        // Task 3: Leave one part empty for the user to solve
        // We'll leave the second number blank
        gridData["$row,$col2"] = ""
        editableCells.add("$row,$col2")
        correctAnswers["$row,$col2"] = val2.toString()

        gridData["$row,$colEq"] = "="
        gridData["$row,$colRes"] = result.toString()
    }

    // This is called when the user types a number into a cell
    fun updateCell(row: Int, col: Int, input: String) {
        val key = "$row,$col"
        if (editableCells.contains(key)) {
            // Check if input is empty or just numbers (prevents crashing)
            if (input.isEmpty() || input.all { it.isDigit() }) {
                gridData[key] = input
                calculateScore()
            }
        }
    }

    // Task 4: Checks if the user's input matches the secret answer
    fun isCellCorrect(row: Int, col: Int): Boolean {
        val key = "$row,$col"
        return gridData[key] == correctAnswers[key]
    }

    // Update the total score based on correct equations
    private fun calculateScore() {
        var currentPoints = 0
        for (key in editableCells) {
            if (gridData[key] == correctAnswers[key]) {
                currentPoints++
            }
        }
        score.value = currentPoints
    }
}