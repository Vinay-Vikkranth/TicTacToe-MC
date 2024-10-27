package com.example.ttt_9

// HistoryActivity.kt
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val historyTextView = findViewById<TextView>(R.id.historyTextView)
        val sharedPref = getSharedPreferences("GameHistory", Context.MODE_PRIVATE)

        // Retrieve all saved results
        val allEntries = sharedPref.all
        val historyBuilder = StringBuilder()

        allEntries.entries.forEach { entry ->
            historyBuilder.append("${entry.value}\n")
        }

        // Display results in the TextView
        historyTextView.text = historyBuilder.toString()
    }
}
