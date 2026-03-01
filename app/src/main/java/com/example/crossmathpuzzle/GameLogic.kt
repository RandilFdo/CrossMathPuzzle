package com.example.crossmathpuzzle

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.random.Random

// Using a ViewModel is the best way to keep data safe when the screen rotates.
// This handles all the "math" behind the scenes.
class GameViewModel : ViewModel() {

    // Task 3: Grid size must be random between 11 and 20
    val gridSize = Random.nextInt(11, 21)

    // This map stores what is in each cell.
    // Key is "row,col" (e.g., "2,5") and value is the text to show.
    var gridData = mutableStateMapOf<String, String>()

    // We need to know which cells are numbers the user is supposed to fill in
    val editableCells = mutableSetOf<String>()

    // We store the correct answers here to check against user input later
    private val solutionKeys = mutableMapOf<String, String>()

    init {
        generateLevel()
    }

    private fun generateLevel() {
        // Clear everything for a fresh start
        gridData.clear()
        editableCells.clear()
        solutionKeys.clear()

        // For now, let's create a few random horizontal equations
        // In a real "Cross Math", we'd loop through and place them strategically.
        for (i in 0 until gridSize step 3) {
            if (i + 4 < gridSize) {
                createHorizontalEquation(row = i, startCol = 1)
            }
        }
    }

    private fun createHorizontalEquation(row: Int, startCol: Int) {
        val operators = listOf("+", "-", "*", "/")
        val op = operators.random()

        val n1 = Random.nextInt(1, 20)
        val n2 = Random.nextInt(1, 20)

        // Calculate the result based on the operator
        val res = when (op) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "*" -> n1 * n2
            else -> if (n2 != 0) n1 / n2 else 0 // Avoid division by zero
        }

        // Place them in the grid
        gridData["$row,$startCol"] = n1.toString()
        gridData["$row,${startCol + 1}"] = op

        // Task 3: One of the numbers must be empty for the user to fill
        val emptyCol = startCol + 2
        gridData["$row,$emptyCol"] = ""
        editableCells.add("$row,$emptyCol")
        solutionKeys["$row,$emptyCol"] = n2.toString() // Save the real answer

        gridData["$row,${startCol + 3}"] = "="
        gridData["$row,${startCol + 4}"] = res.toString()
    }

    // Function to update the cell when the user types a number
    fun onCellChange(row: Int, col: Int, newValue: String) {
        val key = "$row,$col"
        if (editableCells.contains(key)) {
            // Only allow numbers to be entered
            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                gridData[key] = newValue
            }
        }
    }

    // Task 4: Check if the number entered is correct for the feedback colors
    fun isCorrect(row: Int, col: Int): Boolean {
        val key = "$row,$col"
        return gridData[key] == solutionKeys[key]
    }
}