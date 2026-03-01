package com.example.crossmathpuzzle

import androidx.compose.runtime.mutableStateMapOf
import kotlin.random.Random

// This class handles the math and the grid state
class GameLogic {
    // The grid size must be between 11 and 20 as per coursework specs
    val gridSize = Random.nextInt(11, 21)

    // This stores what is in each cell. Key is "row,col", Value is the String
    // Using a Map is easier for a sparse grid than a 2D array
    val gridData = mutableStateMapOf<String, String>()

    // This tracks which cells are meant to be empty for the user to fill
    val userInputCells = mutableSetOf<String>()

    init {
        generatePuzzle()
    }

    private fun generatePuzzle() {
        // For now, let's generate a simple horizontal equation to test
        // Example: 5 + 10 = 15
        val row = 2
        val startCol = 2

        val num1 = Random.nextInt(1, 100)
        val num2 = Random.nextInt(1, 100)
        val result = num1 + num2

        // Fill the grid cells
        gridData["$row,$startCol"] = num1.toString()
        gridData["$row,${startCol + 1}"] = "+"
        gridData["$row,${startCol + 2}"] = "" // This is the blank for the user
        gridData["$row,${startCol + 3}"] = "="
        gridData["$row,${startCol + 4}"] = result.toString()

        // Mark the blank cell so the UI knows to show an input box
        userInputCells.add("$row,${startCol + 2}")
    }

    // Basic check to see if an equation is correct
    // We will expand this as we build the full grid logic
    fun isEquationCorrect(row: Int, col: Int): Boolean {
        // Logic for Task 4 (Green/Red feedback) goes here
        return false
    }
}