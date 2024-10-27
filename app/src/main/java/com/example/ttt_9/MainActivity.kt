// Package declaration for organizing related code files
package com.example.ttt_9

// Import necessary libraries and components
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


// Global variable to determine if the user is in single-player mode
var singleUser = false

// MainActivity class, which serves as the entry point for the app
class MainActivity : AppCompatActivity() {

    // Declare UI components for single and multiplayer buttons
    private lateinit var singlePlayerBtn: Button
    private lateinit var multiPlayerBtn: Button

    // Override the onCreate method to set up the UI when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Set the layout file for this activity

        // Initialize buttons by finding them in the layout
        singlePlayerBtn = findViewById(R.id.idBtnSinglePlayer)
        multiPlayerBtn = findViewById(R.id.idBtnMultiPlayer)

        // Set up click listener for the single-player button
        singlePlayerBtn.setOnClickListener {
            singleUser = true // Set singleUser to true for single-player mode
            startActivity(Intent(this, GameActivity::class.java)) // Start the GameActivity
        }

        // Set up click listener for the multiplayer button
        multiPlayerBtn.setOnClickListener {
            singleUser = false // Set singleUser to false for multiplayer mode
            startActivity(Intent(this, AgainstHumanActivity::class.java)) // Start the AgainstHumanActivity
        }
    }
}
